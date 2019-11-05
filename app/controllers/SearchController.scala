package controllers

import models._
import javax.inject.{Inject, Singleton}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import settings.Utils

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

@Singleton
class SearchController @Inject()(cc: ControllerComponents,
                                 transactionsDao: TransactionsDao,
                                 historyDao: HistoryDao,
                                 boxesDao: BoxesDao)
                                (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getBlock(id: String): Future[Option[Block]] = {
    val headerF: Future[Option[Header]] = historyDao.findHeader(id)
    val payloadF: Future[List[Transaction]] = transactionsDao.transactionsByBlock(id)
    for {
      headerOpt <- headerF
      payload   <- payloadF
    } yield headerOpt match {
      case Some(header) => Some(Block(header, payload))
      case _ => None
    }
  }

  def getFullTransaction(id: String): Future[Option[FullFilledTransaction]] =
    transactionsDao.transactionById(id).flatMap {
      case Some(tx) =>
        val outputsF: Future[List[Output]] = transactionsDao.outputsByTransaction(tx.id)
        val inputsF:  Future[List[Input]]  = transactionsDao.inputsByTransaction(tx.id)
        val contractF: Future[List[Contract]] = transactionsDao.contractByTransaction(tx.id)
        for {
          outputs  <- outputsF
          inputs   <- inputsF
          contract <- contractF
        } yield  Some(FullFilledTransaction(tx, inputs, outputs, contract))

      case _ => Future(Option.empty[FullFilledTransaction])
    }

  def search(id: String): Action[AnyContent] = Action.async {

    def retrieveWallet(id: String): Future[List[Wallet]] = Future
      .fromTry(Try(Utils.contractHashByAddress(id)))
      .flatMap(boxesDao.getWalletByHash)
      .recoverWith {
        case NonFatal(_) => boxesDao.getWalletByHash(id)
      }

    def retrieveTxs(id: String): Future[List[Transaction]] = Future
      .fromTry(Try(Utils.contractHashByAddress(id)))
      .flatMap(boxesDao.getTxsIdByHash)
      .recoverWith {
        case NonFatal(_) => boxesDao.getTxsIdByHash(id)
      }.flatMap { tx =>
        Future.sequence(tx.map(id => boxesDao.getLastTxById(id)))
      }

    val result = for {
      blockOpt       <- getBlock(id)
      transactionOpt <- if (blockOpt.isEmpty) getFullTransaction(id) else Future.successful(None)
      outputOpt      <- if (transactionOpt.isEmpty) transactionsDao.outputById(id) else Future.successful(None)
      wallet         <- if (outputOpt.isEmpty) retrieveWallet(id).map(_.filter(w => w.amount != 0 && w.tokenId.nonEmpty)) else Future.successful(List.empty[Wallet])
      txs            <- if (outputOpt.isEmpty) retrieveTxs(id) else Future.successful(List.empty[Transaction])
    } yield (blockOpt, transactionOpt, outputOpt, wallet, txs)

    result.map {
      case (blockOpt, _, _,_,_) if blockOpt.nonEmpty                             => Ok(views.html.blockInfo(blockOpt.get))
      case (_, transactionOpt, _, _,_) if transactionOpt.nonEmpty                => Ok(views.html.transactionInfo(transactionOpt.get))
      case (_,_,outputOpt,_,_) if outputOpt.nonEmpty                             => Ok(views.html.outputInfo(outputOpt.get))
      case (_, _, _, walletOpt, txsOpt) if walletOpt.nonEmpty && txsOpt.nonEmpty => Ok(views.html.wallet(walletOpt, txsOpt))
      case _ => NotFound
    }
  }
}
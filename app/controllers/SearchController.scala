package controllers

import javax.inject.{Inject, Singleton}
import models.dao.{BoxesDao, HistoryDao, TransactionsDao}
import models.{Block, Contract, DBInput, DBOutput, DBTransaction, FullFilledTransaction, Header, Wallet}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}
import settings.Utils
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SearchController @Inject()(cc: ControllerComponents,
                                 transactionsDao: TransactionsDao,
                                 historyDao: HistoryDao,
                                 boxesDao: BoxesDao)
                                (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getBlock(id: String): Future[Option[Block]] = {
    val headerF: Future[Option[Header]] = historyDao.findHeader(id)
    val payloadF: Future[List[DBTransaction]] = transactionsDao.transactionsByBlock(id)
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
        val outputsF: Future[List[DBOutput]] = transactionsDao.outputsByTransaction(tx.id)
        val inputsF:  Future[List[DBInput]]  = transactionsDao.inputsByTransaction(tx.id)
        val contractF: Future[List[Contract]] = transactionsDao.contractByTransaction(tx.id)
        for {
          outputs  <- outputsF
          inputs   <- inputsF
          contract <- contractF
        } yield  Some(FullFilledTransaction(tx, inputs, outputs, contract))

      case _ => Future(Option.empty[FullFilledTransaction])
    }

  def search(id: String): Action[AnyContent] = Action.async {

    val blockF: Future[Option[Block]] = getBlock(id)
    val transactionF: Future[Option[FullFilledTransaction]] = getFullTransaction(id)
    val outputF: Future[Option[DBOutput]] = transactionsDao.outputById(id)
    val walletF: Future[List[Wallet]] = boxesDao.getWalletByHash(Utils.contractHashByAddress(id))
    val txIdF: Future[List[String]] = boxesDao.getTxsIdByHash(Utils.contractHashByAddress(id))
    val txsF: Future[List[DBTransaction]] = txIdF.flatMap(x => Future.sequence(x.map(id => boxesDao.getLastTxById(id))))

    val result = for {
      blockOpt       <- blockF
      transactionOpt <- transactionF
      outputOpt      <- outputF
      walletOpt      <- walletF
      txsOpt         <- txsF
    } yield (blockOpt, transactionOpt, outputOpt, walletOpt, txsOpt)

    result.map {
      case (blockOpt, _, _,_,_) if blockOpt.nonEmpty              => Ok(views.html.blockInfo(blockOpt.get))
      case (_, transactionOpt, _, _,_) if transactionOpt.nonEmpty => Ok(views.html.transactionInfo(transactionOpt.get))
      case (_,_,outputOpt,_,_) if outputOpt.nonEmpty              => Ok(views.html.outputInfo(outputOpt.get))
      case (_, _, _, walletOpt,txsOpt) if walletOpt.nonEmpty && txsOpt.nonEmpty         => Ok(views.html.wallet(walletOpt, txsOpt))
      case _ => NotFound
    }
  }
}
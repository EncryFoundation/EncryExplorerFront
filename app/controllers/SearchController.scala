package controllers

import models._
import javax.inject.{Inject, Singleton}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SearchController @Inject()(cc: ControllerComponents,
                                 transactionsDao: TransactionsDao,
                                 historyDao: HistoryDao)
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
        val inputsF: Future[List[Input]]   = transactionsDao.inputsByTransaction(tx.id)
        for {
          outputs <- outputsF
          inputs  <- inputsF
        } yield Some(FullFilledTransaction(tx, inputs, outputs))
      case _ => Future(Option.empty[FullFilledTransaction])
    }

  def search(id: String): Action[AnyContent] = Action.async {

    val blockF: Future[Option[Block]] = getBlock(id)
    val transactionF: Future[Option[FullFilledTransaction]] = getFullTransaction(id)
    val outputF: Future[Option[Output]] = transactionsDao.outputById(id)

    val result: Future[(Option[Block], Option[FullFilledTransaction], Option[Output])] = for {
      blockOpt       <- blockF
      transactionOpt <- transactionF
      outputOpt      <- outputF
    } yield (blockOpt, transactionOpt, outputOpt)

    result.map {
      case (blockOpt, _, _) if blockOpt.nonEmpty             => Ok(views.html.blockInfo(blockOpt.get))
      case (_, transactionOpt, _) if transactionOpt.nonEmpty => Ok(views.html.transactionInfo(transactionOpt.get))
      case (_,_,outputOpt) if outputOpt.nonEmpty             => Ok(views.html.outputInfo(outputOpt.get))
      case _ => NotFound
    }
  }
}
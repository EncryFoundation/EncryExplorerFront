package controllers

import models.{Contract, DBInput, DBTransaction, FullFilledTransaction, Header, Output, TransactionsDao}
import javax.inject.{Inject, Singleton}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionsController @Inject()(cc: ControllerComponents,
                                       transactionsDao: TransactionsDao)
                                      (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getTransaction(txId: String): Action[AnyContent] = Action.async {
    transactionsDao.fullTransactionById(txId).map {
      case Some(tx) => Ok(views.html.transactionInfo(tx))
      case None => NotFound
    }
  }

  def getUncomTransactions: Action[AnyContent] = Action.async {
    transactionsDao.unconfirmedTransactions().map(txs => Ok(views.html.transactions(txs)))
  }

  def indByTxId(txId: String): Int = {
    0
  }

  def transactionsPage(txId: String): Action[AnyContent] = Action.async {
//    val range: Future[List[Header]] = historyDao.listHeadersByHeightRange(to)
//    val height: Future[Option[Int]] = historyDao.lastHeights()
//
//    val result: Future[(List[Header], Option[Int])] = for {
//      rangeOpt  <- range
//      heightOpt <- height
//    } yield (rangeOpt, heightOpt)
//
//    result.map {
//      case (rangeOpt, heightOpt) => Ok(views.html.index(rangeOpt, heightOpt.getOrElse(0)))
//      case _ => NotFound
//    }
    Future(Ok(views.html.transactions(List[DBTransaction]())))
  }


}
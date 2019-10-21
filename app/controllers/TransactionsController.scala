package controllers

import models.{Contract, FullFilledTransaction, DBInput, Output, TransactionsDao}
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
    transactionsDao.unconfirmedTransactions().map(txs => Ok(views.html.uncomtransInfo(txs)))
  }

}
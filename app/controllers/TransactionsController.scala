package controllers

import javax.inject.{Inject, Singleton}
import models.dao.TransactionsDao
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class TransactionsController @Inject()(cc: ControllerComponents,
                                       transactionsDao: TransactionsDao)
                                      (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  val TRANSACTION_PER_PAGE = 10

  def getTransaction(txId: String): Action[AnyContent] = Action.async {
    transactionsDao.fullTransactionById(txId).map {
      case Some(tx) => Ok(views.html.transactionInfo(tx))
      case None => NotFound
    }
  }

  def getUnconfirmedTransactions(page: Int): Action[AnyContent] = Action.async {
    val from = page * TRANSACTION_PER_PAGE
    val to = (page + 1) * TRANSACTION_PER_PAGE
    transactionsDao.unconfirmedTransactions(from, to).map { txs =>
      Ok(views.html.transactions(txs, page))
    }
  }

}
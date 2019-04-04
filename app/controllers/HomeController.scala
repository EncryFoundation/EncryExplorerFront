package controllers

import javax.inject.{Inject, _}
import models.{FullFilledTransaction, Header, HistoryDao, Input, Output, Transaction, TransactionsDao}
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               historyDao: HistoryDao,
                               transactionsDao: TransactionsDao)
                              (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers {

  def index(): Action[AnyContent] = Action.async {
    historyDao.lastHeaders(50).map {
      case Nil => NotFound
      case list: List[Header] => Ok(views.html.index(list))
    }
  }

  def getFullTransaction(id: String): Future[Option[FullFilledTransaction]] =
    transactionsDao.transactionById(id).flatMap {
      case Some(tx) =>
        val outputsF: Future[List[Output]] = transactionsDao.outputsByTransaction(tx.id)
        val inputsF: Future[List[Input]] = transactionsDao.inputsByTransaction(tx.id)
        for {
          outputs <- outputsF
          inputs <- inputsF
        } yield Some(FullFilledTransaction(tx, inputs, outputs))
      case _ => Future(Option.empty[FullFilledTransaction])
    }
}

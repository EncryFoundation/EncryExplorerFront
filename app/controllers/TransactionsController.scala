package controllers

import models.{Contract, FullFilledTransaction, Input, Output, TransactionsDao}
import javax.inject.{Inject, Singleton}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionsController @Inject()(cc: ControllerComponents,
                             transactionsDao: TransactionsDao)
                            (implicit ex: ExecutionContext) extends AbstractController(cc)  with Circe {

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

  def getTransaction(txId: String): Action[AnyContent] = Action.async {
    getFullTransaction(txId).map {
      case Some(tx) => Ok(views.html.transactionInfo(tx))
      case None     => NotFound
    }
  }
}
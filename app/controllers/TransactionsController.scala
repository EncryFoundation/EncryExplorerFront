package controllers

import javax.inject.Inject
import models.{FullFilledTransaction, Input, Output, TransactionsDao}
import play.api.libs.circe.Circe
import play.api.mvc._
import javax.inject.Inject
import models._
import play.api.libs.circe.Circe
import play.api.mvc._
import io.circe.syntax._
import io.circe.generic.auto._
import javax.inject.{Inject, Singleton}
import models._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import scala.concurrent.{ExecutionContext, Future}

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
        for {
          outputs <- outputsF
          inputs  <- inputsF
        } yield Some(FullFilledTransaction(tx, inputs, outputs))
      case _ => Future(Option.empty[FullFilledTransaction])
    }

  def getTransaction(txId: String): Action[AnyContent] = Action.async {
    getFullTransaction(txId).map {
      case Some(tx) => Ok(views.html.transactionInfo(tx))
      case None     => NotFound
    }
  }
}
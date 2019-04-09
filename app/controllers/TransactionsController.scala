package controllers

import io.circe.syntax._
import io.circe.generic.auto._
import models.{FullFilledTransaction, Input, Output, Transaction, TransactionsDao}
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

  def findOutputApi(id: String): Action[AnyContent] = Action.async {
    transactionsDao
      .findOutput(id)
      .map {
        case Some(output) => Ok(output.asJson)
        case None => NotFound
      }
  }

  def findOutputsByTxIdApi(id: String): Action[AnyContent] = Action.async {
    transactionsDao
      .findOutputsByTxId(id)
      .map {
        case Nil => NotFound
        case list: List[Output] => Ok(list.asJson)
      }
  }

  def findInputApi(id: String): Action[AnyContent] = Action.async {
    transactionsDao
      .findInput(id)
      .map {
        case Some(input) => Ok(input.asJson)
        case None => NotFound
      }
  }

  def listInputsByTxIdApi(txId: String): Action[AnyContent] = Action.async {
    transactionsDao
      .listInputs(txId)
      .map {
        case Nil => NotFound
        case list: List[Input] => Ok(list.asJson)
      }
  }

  def findTransactionApi(id: String): Action[AnyContent] = Action.async {
    transactionsDao
      .findTransaction(id)
      .map {
        case Some(transaction) => Ok(transaction.asJson)
        case None => NotFound
      }
  }

  def findTransactionByBlockHeightRangeApi(from: Int, to: Int): Action[AnyContent] = Action.async {
    transactionsDao
      .findTransactionByBlockHeightRange(from, to)
      .map {
        case Nil => NotFound
        case list: List[Transaction] => Ok(list.asJson)
      }
  }


  def findOutputByBlockIdApi(id: String): Action[AnyContent] = Action.async {
    transactionsDao
      .findOutputByBlockId(id)
      .map {
        case Nil => NotFound
        case list: List[Output] => Ok(list.asJson)
      }
  }
}
package controllers

import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models._
import io.circe.generic.auto._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlockController @Inject()(cc: ControllerComponents,
                                historyDao: HistoryDao,
                                transactionsDao: TransactionsDao)
                               (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getBlockView(id: String): Action[AnyContent] = Action.async {
    getBlock(id).map {
      case Some(block) => Ok(views.html.blockInfo(block))
      case _ => NotFound
    }
  }

  def findBlockApi(id: String): Action[AnyContent] = Action.async {
    getFullBlockApiApi(id)
      .map {
        case Some(block) => Ok(block.asJson)
        case None => NotFound
      }
  }

  def getFullBlockApiApi(id: String) = {
    val headerF: Future[Option[Header]] = historyDao.findHeader(id)
    val tx: Future[List[Transaction]] = transactionsDao.transactionsByBlock(id)
    val dir: Future[List[FullFilledTransactionApi]] = tx.flatMap(k =>
      Future.sequence(k.map { tx =>
        val b: Future[List[Directive]] = transactionsDao.directivesByTransaction(tx.id)
        val out: Future[List[Output]] = transactionsDao.findOutputsByTxId(tx.id)
        val inp: Future[List[Input]] = transactionsDao.listInputs(tx.id)
        for {
        directive <- b
        output <- out
        input <- inp
        } yield FullFilledTransactionApi(tx, directive, output, input)
      }))
    for
      {
      fullTx <- dir
      header <- headerF
    } yield header match {
      case Some(head) => Some(BlockApi(head, fullTx))
      case _ => None
    }
  }

    def getBlock(id: String): Future[Option[Block]] = {
      val headerF: Future[Option[Header]] = historyDao.findHeader(id)
      val payloadF: Future[List[Transaction]] = transactionsDao.transactionsByBlock(id)
      for {
        headerOpt <- headerF
        payload <- payloadF
      } yield headerOpt match {
        case Some(header) => Some(Block(header, payload))
        case _ => None
      }
    }
  }
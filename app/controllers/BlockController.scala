package controllers

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
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class BlockController @Inject()(cc: ControllerComponents,
                                historyDao: HistoryDao,
                                transactionsDao: TransactionsDao)
                               (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getBlockView(id: String): Action[AnyContent] = Action.async {
    getBlock(id).map {
      case Some(block) =>
        val coinbaseTx: Transaction = block.payload.filter(_.isCoinbase).head
        val fullTx: FullFilledTransaction = Await.result(getFullTransaction(coinbaseTx.id), 1.minutes).get
        val (minerAddress, minerReward) = fullTx.output.head.minerAddress -> fullTx.output.head.monetaryValue
        Ok(views.html.blockInfo(block, minerAddress, minerReward))
      case _ => NotFound
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
}
package models.service

import actors.TransStorage.{TransactionByIdA, TransactionByIdQ, TransactionsA, TransactionsQ}
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import javax.inject._
import models.{DBTransaction, FullFilledTransaction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class TransService @Inject()(@Named("transStorage") transStorage: ActorRef) {
  implicit val timeout: Timeout = 5 seconds

  def getUnconfirmedTransactions(from: Int, to: Int): Future[List[DBTransaction]] =
    (transStorage ? TransactionsQ(from, to)).mapTo[TransactionsA].map(_.txs.map(_.transaction))

  def transById(id: String): Future[Option[FullFilledTransaction]] =
    (transStorage ? TransactionByIdQ(id)).mapTo[TransactionByIdA].map(_.tx)

  def getTransactionById(id: String): Future[Option[DBTransaction]] =
    transById(id).map(_.map(_.transaction))

  def getFullTransactionById(id: String): Future[Option[FullFilledTransaction]] =
    transById(id)
}

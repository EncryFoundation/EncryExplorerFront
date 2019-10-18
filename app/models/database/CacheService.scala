package models.database

import actors.CacheActor.{TransactionByIdA, TransactionByIdQ, UnconfTransactionsA, UnconfTransactionsQ}
import akka.actor.ActorRef
import akka.util.Timeout
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import settings.ExplorerSettings
import akka.pattern.ask
import models.{DBTransaction, FullFilledTransaction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class CacheService @Inject()(config: Configuration, settings: ExplorerSettings, @Named("cache") cache: ActorRef) {
  implicit val timeout: Timeout = 5 seconds

  def getUncommittedTransactions: Future[List[DBTransaction]] =
    (cache ? UnconfTransactionsQ()).mapTo[UnconfTransactionsA].map(_.txs.map(_.transaction))

  def getTransactionById(id: String): Future[Option[DBTransaction]] =
    (cache ? TransactionByIdQ(id)).mapTo[TransactionByIdA].map(_.tx.map(_.transaction))

  def getFullTransactionById(id: String): Future[Option[FullFilledTransaction]] =
    (cache ? TransactionByIdQ(id)).mapTo[TransactionByIdA].map(_.tx)
}

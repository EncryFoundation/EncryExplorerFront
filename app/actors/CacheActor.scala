package actors

import actors.CacheActor._
import akka.actor.{Actor, Props, Timers}
import javax.inject.Inject
import models._
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import settings.ExplorerSettings

import scala.collection.mutable
import scala.concurrent.duration._

class CacheActor @Inject()(settings: ExplorerSettings) extends Actor with Timers {

  //TODO: store in sorted list, iterate until cond
  var unconfTranscactions: mutable.Map[String, FullFilledTransaction] = mutable.Map()

  object Timer
  object Tick

  timers.startPeriodicTimer(Timer, Tick, 10 seconds)

  def receive: Receive = {
    case tx: Transaction =>
      val dbTransaction = DBTransaction(tx, "")
      val dbInputs = tx.inputs.map(input => DBInput(input, tx.encodedId)).toList
      val dbOutputs = Output.getOutputs(tx, "minerAddress")
      val contracts = dbInputs.map(input => Contract(input.contractHash, input.contract)).distinct
      val unconfTranscaction = FullFilledTransaction(dbTransaction, dbInputs, dbOutputs, contracts)
      unconfTranscactions += tx.encodedId -> unconfTranscaction

    case TransactionsQ() =>
      sender ! TransactionsA(unconfTranscactions.values.toList.sortBy(-_.transaction.timestamp))

    case TransactionByIdQ(id) =>
      sender ! TransactionByIdA(unconfTranscactions.get(id))

    case RemoveConfirmedTransactions(txIds) =>
      txIds.foreach(id => unconfTranscactions.remove(id))

    case Tick =>
      val timestamp = System.currentTimeMillis()
      val expiredTxIds = unconfTranscactions.values
        .filter(tx => timestamp - tx.transaction.timestamp > settings.cache.unconfirmedTransactionExpiredInterval.toMillis)
        .map(_.transaction.id)
      expiredTxIds.foreach(unconfTranscactions.remove(_))

  }

}

object CacheActor {
  case class TransactionsQ()
  case class TransactionsA(txs: List[FullFilledTransaction])

  case class TransactionByIdQ(id: String)
  case class TransactionByIdA(tx: Option[FullFilledTransaction])

  case class RemoveConfirmedTransactions(txIds: List[String])
}

package actors

import actors.TransStorage._
import akka.actor.{Actor, Props, Timers}
import javax.inject.Inject
import models.{FullFilledTransaction, _}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import play.api.Logger
import settings.Settings

import scala.collection.mutable
import scala.concurrent.duration._

class TransStorage @Inject()(settings: Settings) extends Actor with Timers {

  var unconfTranscactions: mutable.Map[String, FullFilledTransaction] = mutable.Map[String, FullFilledTransaction]()

  object Timer
  object Tick

  timers.startPeriodicTimer(Timer, Tick, 10 seconds)

  def receive: Receive = {
    case tx: Transaction =>
      unconfTranscactions += tx.encodedId -> FullFilledTransaction(tx)

    case TransactionsQ(from, to) =>
      val fromBound = if (from >= 0) from else 0
      val toBound = if (to <= unconfTranscactions.size) to else unconfTranscactions.size

      val txs = unconfTranscactions.values.toList
        .sortBy(-_.transaction.timestamp)
        .slice(if (fromBound <= toBound) fromBound else toBound, toBound)
      sender ! TransactionsA(txs)

    case TransactionByIdQ(id) =>
      sender ! TransactionByIdA(unconfTranscactions.get(id))

    case RemoveConfirmedTransactions(txIds) =>
      txIds.foreach(id => unconfTranscactions.remove(id))

    case Tick =>
      val timestamp = System.currentTimeMillis()
      val expiredTxIds = unconfTranscactions.values
        .filter(tx => timestamp - tx.transaction.timestamp > settings.trans.unconfirmedTransactionExpiredInterval.toMillis)
        .map(_.transaction.id)
      expiredTxIds.foreach(unconfTranscactions.remove(_))
  }

}

object TransStorage {
  case class TransactionsQ(from: Int, to: Int)
  case class TransactionsA(txs: List[FullFilledTransaction])

  case class TransactionByIdQ(id: String)
  case class TransactionByIdA(tx: Option[FullFilledTransaction])

  case class RemoveConfirmedTransactions(txIds: List[String])
}

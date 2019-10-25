package actors

import actors.TransStorage._
import akka.actor.{Actor, Timers}
import javax.inject.Inject
import models.FullFilledTransaction
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import settings.Settings

import scala.collection.mutable
import scala.concurrent.duration._

class TransStorage @Inject()(settings: Settings) extends Actor with Timers {

  val transactions: mutable.LinkedHashMap[String, FullFilledTransaction] = mutable.LinkedHashMap.empty[String, FullFilledTransaction]

  object Timer

  timers.startPeriodicTimer(Timer, Tick, 10 seconds)

  def receive: Receive = {
    case tx: Transaction =>
      transactions += tx.encodedId -> FullFilledTransaction(tx)

    case TransactionsQ(from, to) =>
      val fromBound = if (from >= 0) from else 0
      val toBound = if (to <= transactions.size) to else transactions.size
      val fromBoundR = transactions.size - toBound
      val toBoundR = transactions.size - (if (fromBound <= toBound) fromBound else toBound)
      val txs = transactions.values.slice(fromBoundR, toBoundR).toList.reverse
      sender ! TransactionsA(txs)

    case TransactionByIdQ(id) =>
      sender ! TransactionByIdA(transactions.get(id))

    case RemoveConfirmedTransactions(txIds) =>
      txIds.foreach(id => transactions.remove(id))

    case Tick =>
      val timestamp = System.currentTimeMillis()

      transactions.takeWhile { case (_, tx) =>
        timestamp - tx.transaction.timestamp > settings.trans.unconfirmedTransactionExpiredInterval.toMillis
      }.foreach { case (_, tx) =>
        transactions.remove(tx.transaction.id)
      }
  }
}

object TransStorage {
  case object Tick

  case class TransactionsQ(from: Int, to: Int)
  case class TransactionsA(txs: List[FullFilledTransaction])

  case class TransactionByIdQ(id: String)
  case class TransactionByIdA(tx: Option[FullFilledTransaction])

  case class RemoveConfirmedTransactions(txIds: List[String])
}

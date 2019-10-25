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

  val transTs: mutable.TreeMap[Long, FullFilledTransaction] = mutable.TreeMap.empty[Long, FullFilledTransaction]
  val transIds: mutable.TreeMap[String, Long] = mutable.TreeMap.empty[String, Long]

  object Timer

  timers.startPeriodicTimer(Timer, Tick, 10 seconds)

  def receive: Receive = {
    case tx: Transaction =>
      transTs += tx.timestamp -> FullFilledTransaction(tx)
      transIds += tx.encodedId -> tx.timestamp

    case TransactionsQ(from, to) =>
      val fromBound = if (from >= 0) from else 0
      val toBound = if (to <= transTs.size) to else transTs.size
      val fromBoundR = transTs.size - toBound
      val toBoundR = transTs.size - (if (fromBound <= toBound) fromBound else toBound)

      val txs = transTs.values.slice(fromBoundR, toBoundR).toList.reverse
      sender ! TransactionsA(txs)

    case TransactionByIdQ(id) =>
      val tx = transIds.get(id).flatMap(transTs.get)
      sender ! TransactionByIdA(tx)

    case RemoveConfirmedTransactions(txIds) =>
      val tsList = txIds.flatMap(transIds.get)
      tsList.foreach(ts => transTs.remove(ts))
      txIds.foreach(id => transIds.remove(id))

    case Tick =>
      val timestamp = System.currentTimeMillis()

      transTs.takeWhile { case (ts, _) =>
        timestamp - ts > settings.trans.unconfirmedTransactionExpiredInterval.toMillis
      }.foreach { case (ts, tx) =>
        transIds.remove(tx.transaction.id)
        transTs.remove(ts)
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

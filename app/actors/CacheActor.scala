package actors

import actors.CacheActor.{RemoveConfirmedTransactions, TransactionsA, TransactionsQ}
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.StrictLogging
import models.DBTransaction

import scala.collection.mutable

class CacheActor extends Actor with StrictLogging {

  var transcactions: mutable.Map[String, DBTransaction] = mutable.Map()

  def receive: Receive = {
    case tx: DBTransaction =>
      transcactions += tx.id -> tx

    case TransactionsQ() =>
      sender ! TransactionsA(transcactions.values.toSeq)

    case RemoveConfirmedTransactions(txIds) =>
      txIds.foreach(id => transcactions.remove(id))
  }

}

object CacheActor {
  case class TransactionsQ()
  case class TransactionsA(txs: Seq[DBTransaction])

  case class RemoveConfirmedTransactions(txIds: Seq[String])

  def props: Props = Props[ReceiverActor]
}

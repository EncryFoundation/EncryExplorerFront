package actors

import actors.ModifierMessages._
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.StrictLogging
import models.DBTransaction
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import org.encryfoundation.common.utils.Algos

import scala.collection.mutable

class ReceiverActor extends Actor with StrictLogging {

  var transcactions: mutable.Map[String, models.DBTransaction] = mutable.Map(
    "qwe" -> DBTransaction("qwe", 3, "blockId", true, System.currentTimeMillis(), None)
  )

  def receive: Receive = {
    case ModifierTx(tx: Transaction) =>
//      val trans = models.Transaction(
//        Algos.encode(tx.id),
//        tx.fee,
//        "blockId",
//        tx.
//      )
//      transcactions +=
    case ModifierHeader(header: Header) => println(s"header: $header")
    case ModifierPayload(payload: Payload) => println(s"payload: txs ${payload.txs.size}")

    case TransactionsQ() => sender ! TransactionsA(transcactions.values.toList)
  }
}

object ReceiverActor {
  def props = Props[ReceiverActor]
}

object ModifierMessages {
  case class ModifierTx(tx: Transaction)
  case class ModifierHeader(header: Header)
  case class ModifierPayload(payload: Payload)

  case class TransactionsQ()
  case class TransactionsA(txs: List[models.DBTransaction])
}

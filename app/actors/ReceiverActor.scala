package actors

import actors.ModifierMessages._
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.StrictLogging
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction

import scala.collection.mutable

class ReceiverActor extends Actor with StrictLogging {

  //logger.debug("ExplorerActor started")

  var transcactions: mutable.Map[String, models.Transaction] = mutable.Map(
    "qwe" -> models.Transaction("qwe", 3, "dsfdsffdsf", true, System.currentTimeMillis(), Some("Proof"))
  )

  def receive: Receive = {
    case ModifierTx(tx: Transaction) => println(s"tx: $tx")
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
  case class TransactionsA(txs: List[models.Transaction])
}

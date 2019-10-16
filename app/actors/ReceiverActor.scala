package actors

import actors.ModifierMessages._
import akka.actor.Actor
import com.typesafe.scalalogging.StrictLogging
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction

class ReceiverActor extends Actor with StrictLogging {

  logger.debug("ExplorerActor started")

  def receive: Receive = {
    case ModifierTx(tx: Transaction) => println(s"tx: $tx")
    case ModifierHeader(header: Header) => println(s"header: $header")
    case ModifierPayload(payload: Payload) => println(s"payload: txs ${payload.txs.size}")
  }
}

object ModifierMessages {
  case class ModifierTx(tx: Transaction)
  case class ModifierHeader(header: Header)
  case class ModifierPayload(payload: Payload)
}

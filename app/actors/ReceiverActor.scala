package actors

import actors.CacheActor.RemoveConfirmedTransactions
import akka.actor.{Actor, ActorRef, Props}
import com.typesafe.scalalogging.StrictLogging
import javax.inject.{Inject, Named}
import models.DBTransaction
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction

class ReceiverActor @Inject()(@Named("cache") cache: ActorRef) extends Actor with StrictLogging {

  def receive: Receive = {
    case tx: Transaction =>
      cache ! tx

    case header: Header =>
      logger.debug(s"header: ${header.encodedId}")

    case payload: Payload =>
      cache ! RemoveConfirmedTransactions(payload.txs.map(_.encodedId).toList)
  }

}

object ReceiverActor {
  def props(cache: ActorRef): Props = Props(new ReceiverActor(cache))
}

object ModifierMessages {
  case class ModifierTx(tx: Transaction)
  case class ModifierHeader(header: Header)
  case class ModifierPayload(payload: Payload)
}

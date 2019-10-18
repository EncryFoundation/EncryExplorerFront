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
      val trans = DBTransaction(tx, "")
      cache ! trans

    case header: Header =>
      logger.debug(s"header: ${header.encodedId}")

    case payload: Payload =>
      logger.debug(s"payload: ${payload.encodedId} txs ${payload.txs.size}")
      cache ! RemoveConfirmedTransactions(payload.txs.map(_.encodedId))

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

package actors

import actors.CacheActor.RemoveConfirmedTransactions
import akka.actor.{Actor, ActorRef, Props}
import com.typesafe.scalalogging.StrictLogging
import javax.inject.{Inject, Named}
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction

class ReceiverActor @Inject()(@Named("cache") cache: ActorRef) extends Actor with StrictLogging {

  def receive: Receive = {
    case transaction: Transaction =>
      cache ! transaction

    case confirmedTransactionIds: List[String] =>
      cache ! RemoveConfirmedTransactions(confirmedTransactionIds)
  }

}

object ReceiverActor {
  def props(cache: ActorRef): Props = Props(new ReceiverActor(cache))
}
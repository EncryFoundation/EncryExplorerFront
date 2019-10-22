package actors

import actors.TransStorage.RemoveConfirmedTransactions
import akka.actor.{Actor, ActorRef, Props}
import javax.inject.{Inject, Named}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import play.api.Logger

class Receiver @Inject()(@Named("transStorage") cache: ActorRef) extends Actor {
  Logger.info("Receiver")

  def receive: Receive = {
    case transaction: Transaction =>
      cache ! transaction

    case confirmedTransactionIds: List[String] =>
      cache ! RemoveConfirmedTransactions(confirmedTransactionIds)
  }

}

object Receiver {
  def props(cache: ActorRef): Props = Props(new Receiver(cache))
}
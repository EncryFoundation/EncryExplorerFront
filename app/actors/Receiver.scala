package actors

import actors.TransStorage.RemoveConfirmedTransactions
import akka.actor.{Actor, ActorRef, Props}
import javax.inject.{Inject, Named}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import play.api.Logger

class Receiver @Inject()(@Named("transStorage") transStorage: ActorRef) extends Actor {

  def receive: Receive = {
    case transaction: Transaction =>
      transStorage ! transaction

    case confirmedTransactionIds: List[String] =>
      transStorage ! RemoveConfirmedTransactions(confirmedTransactionIds)
  }
}

object Receiver {
  def props(transStorage: ActorRef): Props = Props(new Receiver(transStorage))
}
package actors

import actors.CacheActor.{RemoveConfirmedTransactions, TransactionByIdA, TransactionByIdQ, UnconfTransactionsA, UnconfTransactionsQ}
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.StrictLogging
import models.{Contract, DBInput, DBTransaction, FullFilledTransaction, Output}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction

import scala.collection.mutable

class CacheActor extends Actor with StrictLogging {

  var unconfTranscactions: mutable.Map[String, FullFilledTransaction] = mutable.Map()

  def receive: Receive = {
    case tx: Transaction =>
      val dbTransaction = DBTransaction(tx, "")
      val dbInputs = tx.inputs.map(input => DBInput(input, tx.encodedId)).toList
      val dbOutputs = Output.getOutputs(tx, "minerAddress")
      val contracts = dbInputs.map(input => Contract(input.contractHash, input.contract)).distinct
      val unconfTranscaction = FullFilledTransaction(dbTransaction, dbInputs, dbOutputs, contracts)
      unconfTranscactions += tx.encodedId -> unconfTranscaction

    case UnconfTransactionsQ() =>
      sender ! UnconfTransactionsA(unconfTranscactions.values.toList)

    case TransactionByIdQ(id) =>
      sender ! TransactionByIdA(unconfTranscactions.get(id))

    case RemoveConfirmedTransactions(txIds) =>
      txIds.foreach(id => unconfTranscactions.remove(id))
  }

}

object CacheActor {
  case class UnconfTransactionsQ()
  case class UnconfTransactionsA(txs: List[FullFilledTransaction])

  case class TransactionByIdQ(id: String)
  case class TransactionByIdA(tx: Option[FullFilledTransaction])

  case class RemoveConfirmedTransactions(txIds: List[String])

  def props: Props = Props[ReceiverActor]
}

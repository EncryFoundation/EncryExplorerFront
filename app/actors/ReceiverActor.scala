package actors

import actors.ModifierMessages._
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.StrictLogging
import models.DBTransaction
import org.encryfoundation.common.modifiers.history.{Header, Payload}
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import org.encryfoundation.common.utils.TaggedTypes.ModifierId

import scala.collection.mutable

class ReceiverActor extends Actor with StrictLogging {

  var transcactions: mutable.Map[String, DBTransaction] = mutable.Map(
    "qwe" -> DBTransaction("qwe", 3, "blockId", true, System.currentTimeMillis(), None)
  )

  def receive: Receive = {
    case tx: Transaction =>
      val trans = DBTransaction(tx, "")
      transcactions += trans.id -> trans

    case header: Header =>
      logger.debug(s"header: ${header.encodedId}")

    case payload: Payload =>
      logger.debug(s"payload: ${payload.encodedId} txs ${payload.txs.size}")
      removeCommitedTrans(payload.txs)

    case TransactionsQ() =>
      sender ! TransactionsA(transcactions.values.toList)
  }

  def removeCommitedTrans(txs: Seq[Transaction]) {
    txs.foreach(tx => transcactions.remove(tx.encodedId))
  }
}

object ReceiverActor {
  def props: Props = Props[ReceiverActor]
}

object ModifierMessages {

  case class EmptyMsg()
  case class ModifierTx(tx: Transaction)
  case class ModifierHeader(header: Header)
  case class ModifierPayload(payload: Payload)

  case class TransactionsQ()
  case class TransactionsA(txs: List[models.DBTransaction])
}

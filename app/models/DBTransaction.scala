package models

import org.encryfoundation.common.modifiers.mempool.transaction.{Proof, Transaction}
import org.encryfoundation.common.utils.Algos
import org.encryfoundation.prismlang.core.wrapped.BoxedValue.{BoolValue, ByteCollectionValue, ByteValue, IntValue, MultiSignatureValue, Signature25519Value, StringValue}

case class DBTransaction(id: String,
                         fee: Long,
                         blockId: String,
                         isCoinbase: Boolean,
                         timestamp: Long,
                         defaultProofOpt: Option[String])

object DBTransaction {

  def apply(tx: Transaction, blockId: String): DBTransaction =
    DBTransaction(
      tx.encodedId,
      tx.fee,
      blockId,
      tx.inputs.isEmpty,
      tx.timestamp,
      tx.defaultProofOpt.map {
        case Proof(IntValue(value), _) => s"IntValue - ${value.toString}"
        case Proof(ByteValue(value), _) => s"ByteValue - ${value.toString}"
        case Proof(BoolValue(value), _) => s"BoolValue - ${value.toString}"
        case Proof(StringValue(value), _) => s"StringValue - ${value.toString}"
        case Proof(ByteCollectionValue(value), _) => s"ByteCollectionValue - ${Algos.encode(value.toArray)}"
        case Proof(Signature25519Value(value), _) => s"Signature25519Value - ${Algos.encode(value.toArray)}"
        case Proof(MultiSignatureValue(value), _) => s"MultiSignatureValue - ${Algos.encode(value.flatten.toArray)}"
      }
    )
}

case class FullFilledTransaction(transaction: DBTransaction, inputs: List[DBInput], output: List[Output], contract: List[Contract])


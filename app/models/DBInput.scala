package models

import org.encryfoundation.common.modifiers.mempool.transaction.Input
import org.encryfoundation.common.utils.Algos

case class DBInput(bxId: String, txId: String, contractHash: String, proofs: String)

object DBInput {
  def apply(input: Input, txId: String): DBInput =
    new DBInput(
      Algos.encode(input.boxId),
      txId,
      input.contract.fold(c => Algos.encode(c.hash), c => Algos.encode(c.contract.hash)),
      input.proofs.map(_.toString).mkString(",")
    )

  def apply(bxId: String, txId: String, contractHash: String, proofs: String): DBInput = new DBInput(bxId, txId, contractHash, proofs)
}
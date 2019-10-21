package models

import models.Output.Proposition
import org.encryfoundation.common.modifiers.mempool.transaction.EncryAddress.Address
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import org.encryfoundation.common.modifiers.state.box.AssetBox
import org.encryfoundation.common.utils.Algos
import settings.Utils

case class Output(idx: Int,
                  id: String,
                  `type`: Long,
                  txId: String,
                  value: Long,
                  nonce: Long,
                  tokenId: String,
                  proposition: Proposition,
                  data: String,
                  isActive: Boolean,
                  minerAddress: String)

object Output{

  case class Proposition(contractHash: String)

  def apply(idx: Int,
            id: String,
            `type`: Long,
            txId: String,
            value: Long,
            nonce: Long,
            tokenId: String,
            proposition: String,
            data: String,
            isActive: Boolean,
            minerAddress: String): Output = new Output(idx, id, `type`, txId, value, nonce, tokenId, Proposition(proposition), data, isActive, minerAddress)

  def getOutputs(tx: Transaction, minerAddress: Address): List[Output] = {
    //val box: EncryBaseBox = directive.boxes(Digest32 @@ Algos.decode(tx.id).getOrElse(Array.emptyByteArray), idx)
    tx.newBoxes.toList.zipWithIndex.map { case(box, idx) =>
      val (amount, tokenId) = box match {
        case box: AssetBox =>
          (box.amount, Algos.encode(box.tokenIdOpt.getOrElse(Utils.IntrinsicTokenId)))
        case _ => (0L, "")
      }
      new Output(idx, tx.encodedId, box.typeId, tx.encodedId, amount, box.nonce, tokenId,
        Proposition(Algos.encode(box.proposition.contractHash)),
        "box.data", true, minerAddress)
    }
  }
}
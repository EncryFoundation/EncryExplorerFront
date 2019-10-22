package models

import models.Output.Proposition
import org.encryfoundation.common.modifiers.mempool.directive.TransferDirective
import org.encryfoundation.common.modifiers.mempool.transaction.EncryAddress.Address
import org.encryfoundation.common.modifiers.mempool.transaction.Transaction
import org.encryfoundation.common.modifiers.state.box.{AssetBox, DataBox}
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

object Output {

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

  def apply(tx: Transaction): List[Output] = {
    tx.directives.toList.collect {
      case TransferDirective(address, amount, tokenIdOpt) => address
    }

    tx.newBoxes.toList.zipWithIndex.map { case(box, idx) =>
      val (amount, tokenId, data) = box match {
        case box: AssetBox =>
          (box.amount, Algos.encode(box.tokenIdOpt.getOrElse(Utils.IntrinsicTokenId)), "")
        case box: DataBox =>
          (0L, "", Algos.encode(box.data))
        case _ => (0L, "", "")
      }
      val address: Option[Address] = tx.directives(idx) match {
        case d: TransferDirective => Some(d.address)
        case _ => None
      }

      new Output(idx, Algos.encode(box.id), box.typeId, tx.encodedId, amount, box.nonce, tokenId,
        Proposition(Algos.encode(box.proposition.contractHash)), data, true, address.getOrElse(""))
    }
  }
}
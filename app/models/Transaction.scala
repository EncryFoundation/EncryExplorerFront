package models

import java.text.DecimalFormat
import settings.Utils.ETTid

case class Transaction(id: String,
                       fee: Long,
                       blockId: String,
                       isCoinbase: Boolean,
                       timestamp: Long,
                       defaultProofOpt: Option[String]) {

  lazy val formatedFee: String = {
    val df = new DecimalFormat()
    df.setMaximumFractionDigits(10)

    s"${df.format(BigDecimal(fee) / 100000000)} ETT"
  }
}

case class FullFilledTransaction(transaction: Transaction, inputs: List[Input], output: List[Output], contract: List[Contract]) {
  val fee: String = transaction.formatedFee

  val transfers: List[(String, String, String)] = output.foldLeft(Map.empty[(String, String), Long].withDefaultValue(0L)) { case (tr, out) =>
    if (out.value == 0) tr
    else tr.updated((out.address, out.tokenId), tr(out.address, out.tokenId) + out.value)
  }.toList.map { case ((addr, tok), amount) =>
    if (tok == ETTid) (addr, tok, (BigDecimal(amount) / 100000000).toString())
    else (addr, tok, amount.toString)
  }

}
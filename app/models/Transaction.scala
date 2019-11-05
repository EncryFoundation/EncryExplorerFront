package models

import java.text.DecimalFormat

case class Transaction(id: String,
                       fee: Long,
                       blockId: String,
                       isCoinbase: Boolean,
                       timestamp: Long,
                       defaultProofOpt: Option[String])

case class FullFilledTransaction(transaction: Transaction, inputs: List[Input], output: List[Output], contract: List[Contract]) {
  private val df = new DecimalFormat()
  df.setMaximumFractionDigits(10)

  val fee: String = df.format(BigDecimal(transaction.fee) / 100000000)

  val transfers: List[(String, String, String)] = output.foldLeft(Map.empty[(String, String), Long].withDefaultValue(0L)) { case (tr, out) =>
    if (out.value == 0) tr
    else tr.updated((out.address, out.tokenId), tr(out.address, out.tokenId) + out.value)
  }.toList.map { case ((addr, tok), amount) => (addr, tok, (BigDecimal(amount) / 100000000).toString())}

}
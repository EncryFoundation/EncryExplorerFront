package models

case class Transaction(id: String,
                       fee: Long,
                       blockId: String,
                       isCoinbase: Boolean,
                       timestamp: Long,
                       defaultProofOpt: Option[String])

case class FullFilledTransaction(transaction: Transaction, inputs: List[Input], output: List[Output])

case class FullFilledTransactionApi(id: String,
                                    fee: Long,
                                    blockId: String,
                                    timestamp: Long,
                                    defaultProofOpt: Option[String],
                                    directive: List[Directive],
                                    output: List[Output],
                                    input: List[Input])

object FullFilledTransactionApi {
  def apply(transaction: Transaction, directive: List[Directive], output: List[Output], input: List[Input]): FullFilledTransactionApi =
    FullFilledTransactionApi(transaction.id,
                             transaction.fee,
                             transaction.blockId,
                             transaction.timestamp,
                             transaction.defaultProofOpt,
                             directive,
                             output,
                             input)
}
package models

case class Transaction(id: String,
                       fee: Long,
                       blockId: String,
                       isCoinbase: Boolean,
                       timestamp: Long,
                       defaultProofOpt: String)

case class FullFilledTransaction(transaction: Transaction, inputs: List[Input], output: List[Output])
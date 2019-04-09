package models

case class Directive(txId: String,
                     numberInTx: Int,
                     dTypeId: Byte,
                     isValid: Boolean,
                     contractHash: String,
                     amount: Long,
                     address: String,
                     tokenIdOpt: Option[String],
                     data: String)
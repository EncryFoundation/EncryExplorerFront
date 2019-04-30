package models

import io.circe.Encoder
import io.circe.syntax._

case class Output(id: String,
                  boxType: Long,
                  txId: String,
                  monetaryValue: Long,
                  nonce: Long,
                  coinId: String,
                  contractHash: String,
                  data: String,
                  isActive: Boolean,
                  minerAddress: String)
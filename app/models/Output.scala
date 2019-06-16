package models

import io.circe.generic.auto._
import io.circe.Encoder
import io.circe.syntax._
import models.Output.Proposition

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
}
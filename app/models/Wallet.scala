package models

import settings.Utils

case class Wallet (idx: Int,
                   hash: String,
                   amount: Long,
                   tokenId: String)

object Wallet{
  val ett: String = Utils.EttTokenId
  def apply(idx: Int, hash: String, amount: Long, tokenId: String): Wallet = new Wallet(
    idx,
    hash,
    amount,
    tokenId match {
      case encry if tokenId == ett => s"ETT"
      case _ => tokenId
    }
  )
}
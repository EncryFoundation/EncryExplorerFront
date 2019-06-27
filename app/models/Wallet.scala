package models

import settings.Utils

case class Wallet (hash: String,
                   amount: Long,
                   tokenId: String)

object Wallet{
  val ett: String = Utils.EttTokenId
  def apply(hash: String, amount: Long, tokenId: String): Wallet = new Wallet(
    hash,
    amount,
    tokenId match {
      case encry if tokenId == ett => s"ETT"
      case _ => tokenId
    }
  )
}
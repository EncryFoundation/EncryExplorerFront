package models.database

import doobie._
import doobie.implicits._
import models.{Transaction, Wallet}


object WalletQueries {

  def getWallet(address: String): ConnectionIO[List[Wallet]] =
    sql"SELECT * FROM wallet WHERE hash = $address".query[Wallet].to[List]

  def getTxsIdByHash(address: String): ConnectionIO[List[String]] =
    sql"SELECT txId FROM outputs WHERE contracthash = $address".query[String].to[List]

  def getLastTxsByHash(txId: String): ConnectionIO[Option[Transaction]] =
    sql"SELECT * FROM transactions WHERE id = $txId ORDER BY timestamp LIMIT 10".query[Transaction].to[List].map(_.headOption)

}

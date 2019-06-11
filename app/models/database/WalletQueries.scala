package models.database

import doobie._
import doobie.implicits._
import models.{Transaction, Wallet}


object WalletQueries {

  def getWallet(address: String): ConnectionIO[List[Wallet]] =
    sql"SELECT * FROM wallet WHERE hash = $address".query[Wallet].to[List]

  def getTxsIdByHash(address: String): ConnectionIO[List[String]] =
    sql"SELECT txId FROM outputs WHERE contractHash = $address order by idx DESC LIMIT 50".query[String].to[List]

  def getLastTxsByHash(txId: String): ConnectionIO[Transaction] =
    sql"SELECT * FROM transactions WHERE id = $txId ORDER BY timestamp DESC LIMIT 50".query[Transaction].unique

}

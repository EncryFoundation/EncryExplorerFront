package models.database

import doobie._
import doobie.implicits._
import models.Wallet


object WalletQueries {

  def getWallet(address: String): ConnectionIO[List[Wallet]] =
    sql"SELECT * FROM wallet WHERE hash = $address".query[Wallet].to[List]

}

package models.database

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.Input

object InputQueries {

  def findInputQuery(id: String): ConnectionIO[Option[Input]] =
    sql"SELECT * FROM inputs WHERE id = $id".query[Input].to[List].map(_.headOption)

  def listInputsByTxIdQuery(txId: String): ConnectionIO[List[Input]] =
    sql"SELECT * FROM inputs WHERE txid = $txId".query[Input].to[List]
}
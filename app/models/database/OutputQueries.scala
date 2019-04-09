package models.database

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.Output

object OutputQueries {

  def findOutputQuery(id: String): ConnectionIO[Option[Output]] =
    sql"SELECT * FROM outputs WHERE id = $id".query[Output].to[List].map(_.headOption)

  def findOutputsByTxIdQuery(id: String): ConnectionIO[List[Output]] =
    sql"SELECT * FROM outputs WHERE txid = $id".query[Output].to[List]

  def findOutputByBlockIdQuery(id: String): ConnectionIO[List[Output]] =
    sql"SELECT * FROM outputs WHERE txid IN (SELECT id FROM transactions WHERE blockid = $id)".query[Output].to[List]
}
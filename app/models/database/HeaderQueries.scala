package models.database

import doobie._
import doobie.implicits._
import models.Header

object HeaderQueries {

  def lastBlocks(qty: Int = 50): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers ORDER BY height DESC limit $qty ".query[Header].to[List]

  def findByIdQuery(id: String): ConnectionIO[Option[Header]] =
    sql"SELECT * FROM headers WHERE id = $id".query[Header].to[List].map(_.headOption)

}

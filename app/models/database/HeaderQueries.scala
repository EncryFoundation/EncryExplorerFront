package models.database

import doobie._
import doobie.implicits._
import models.Header

object HeaderQueries {

  def lastBlocks(qty: Int = 50): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers ORDER BY height DESC limit $qty ".query[Header].to[List]

  def lastHeight(): ConnectionIO[Option[Int]] =
    sql"SELECT height FROM headers ORDER BY height DESC limit 1".query[Int].to[List].map(_.headOption)

  def findByIdQuery(id: String): ConnectionIO[Option[Header]] =
    sql"SELECT * FROM headers WHERE id = $id".query[Header].to[List].map(_.headOption)

  def findByHeightRangeQuery(to: Int): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers WHERE height BETWEEN $to - 49 AND $to  ORDER BY height DESC".query[Header].to[List]
}

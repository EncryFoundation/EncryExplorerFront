package models.database

import doobie._
import doobie.implicits._
import models.Header

object HeaderQueries {

  def lastBlocks(qty: Int = 50): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers ORDER BY height DESC limit $qty ".query[Header].to[List]

  def findByIdQuery(id: String): ConnectionIO[Option[Header]] =
    sql"SELECT * FROM headers WHERE id = $id".query[Header].to[List].map(_.headOption)

  def findByCountQuery(from: Int, count: Int): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers WHERE (height BETWEEN $from AND ($from + $count - 1)) ORDER BY height DESC".query[Header].to[List]

  def findByHeightQuery(height: Int): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers WHERE height = $height ORDER BY height DESC".query[Header].to[List]

  def findLastByHeightQuery(height: Int): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers WHERE best_chain = TRUE ORDER BY height DESC LIMIT $height".query[Header].to[List]

  def findByHeightRangeQuery(from: Int, to: Int): ConnectionIO[List[Header]] =
    sql"SELECT * FROM headers WHERE height BETWEEN $from AND $to".query[Header].to[List]
}

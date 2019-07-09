package models.database

import doobie._
import doobie.implicits._
import models.Node

object NodeQueries {

  def getNodes: ConnectionIO[List[Node]] =
    sql"SELECT * FROM nodes".query[Node].to[List]

}

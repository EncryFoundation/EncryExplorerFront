package models.database

import doobie._
import doobie.implicits._
import models.Output

object BoxesQueries {

  def getAllBoxesIdsByNodeIp(nodeIp: String): ConnectionIO[List[String]] =
    sql"SELECT outputId FROM outputsToNodes WHERE nodeIp = $nodeIp".query[String].to[List]

  def a(nodeIp: String) =
    sql"SELECT * FROM outputs WHERE outputsToNodes.nodeIp = $nodeIp AND outputsToNodes.outputId = outputs.id"

  def getAvailableBoxByBoxId(boxId: String): ConnectionIO[Option[Output]] =
    sql"SELECT * FROM outputs WHERE id = $boxId AND outputs.isActive = true".query[Output].to[List].map(_.headOption)

  def getNumberOfAvailableBoxesByBoxId(boxId: String, numberOf: Int): ConnectionIO[Option[Output]] =
    sql"SELECT * FROM outputs WHERE id = $boxId AND outputs.isActive = true".query[Output].to[List].map(_.headOption)
}

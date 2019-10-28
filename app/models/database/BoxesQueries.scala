package models.database

import doobie._
import doobie.implicits._
import models.DBOutput

object BoxesQueries {

  def getAllBoxesIdsByNodeIp(nodeIp: String): ConnectionIO[List[String]] =
    sql"SELECT outputId FROM outputsToNodes WHERE nodeIp = $nodeIp".query[String].to[List]

  def a(nodeIp: String) =
    sql"SELECT * FROM outputs WHERE outputsToNodes.nodeIp = $nodeIp AND outputsToNodes.outputId = outputs.id"

  def getAvailableBoxByBoxId(boxId: String): ConnectionIO[Option[DBOutput]] =
    sql"SELECT * FROM outputs WHERE id = $boxId AND outputs.isActive = true".query[DBOutput].to[List].map(_.headOption)

  def getNumberOfAvailableBoxesByBoxId(boxId: String, numberOf: Int): ConnectionIO[Option[DBOutput]] =
    sql"SELECT * FROM outputs WHERE id = $boxId AND outputs.isActive = true".query[DBOutput].to[List].map(_.headOption)

  def getBoxesByContractHash(contractHash: String, from: Int, to: Int): ConnectionIO[List[DBOutput]] =
    sql"SELECT * FROM outputs WHERE contractHash = $contractHash AND outputs.isActive = true limit ${to - from} offset $from".query[DBOutput].to[List]
}

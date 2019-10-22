package models.dao

import javax.inject.Inject
import models.database.HeaderQueries._
import models.database.{DBService, NodeQueries}
import models.{Header, Node}

import scala.concurrent.{ExecutionContext, Future}

class HistoryDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext) {

  def lastHeaders(qty: Int = 50): Future[List[Header]] = dBService.runAsync(lastBlocks(qty))

  def lastHeights(): Future[Option[Int]] = dBService.runAsync(lastHeight())

  def findHeader(id: String): Future[Option[Header]] = dBService.runAsync(findByIdQuery(id))

  def getAllNodes: Future[List[Node]] = dBService.runAsync(NodeQueries.getNodes)

  def listHeadersByHeightRange(to: Int): Future[List[Header]] = dBService.runAsync(findByHeightRangeQuery(to))

}

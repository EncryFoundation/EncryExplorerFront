package models

import javax.inject.Inject
import models.database.DBService
import models.database.HeaderQueries._
import scala.concurrent.{ExecutionContext, Future}

class HistoryDao @Inject()(dBService: DBService)(implicit ec: ExecutionContext) {

  def lastHeaders(qty: Int = 50): Future[List[Header]] = dBService.runAsync(lastBlocks(qty))

  def findHeader(id: String): Future[Option[Header]] = dBService.runAsync(findByIdQuery(id))

  def listHeadersByCount(from: Int, count: Int): Future[List[Header]] = dBService.runAsync(findByCountQuery(from, count))

  def listHeadersAtHeight(height: Int): Future[List[Header]] = dBService.runAsync(findByHeightQuery(height))

  def listLastHeadersByHeight(qty: Int): Future[List[Header]] = dBService.runAsync(findLastByHeightQuery(qty))

  def listHeadersByHeightRange(from: Int, to: Int): Future[List[Header]] = dBService.runAsync(findByHeightRangeQuery(from, to))

}

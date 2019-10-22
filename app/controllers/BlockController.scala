package controllers

import javax.inject.{Inject, Singleton}
import models.{Block, DBTransaction, Header}
import models.dao.{HistoryDao, TransactionsDao}
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlockController @Inject()(cc: ControllerComponents,
                                historyDao: HistoryDao,
                                transactionsDao: TransactionsDao)
                               (implicit ex: ExecutionContext) extends AbstractController(cc) with Circe {

  def getBlockView(id: String): Action[AnyContent] = Action.async {
    getBlock(id).map {
      case Some(block) => Ok(views.html.blockInfo(block))
      case _ => NotFound
    }
  }

  def getBlock(id: String): Future[Option[Block]] = {
    val headerF: Future[Option[Header]] = historyDao.findHeader(id)
    val payloadF: Future[List[DBTransaction]] = transactionsDao.transactionsByBlock(id)
    for {
      headerOpt <- headerF
      payload <- payloadF
    } yield headerOpt match {
      case Some(header) => Some(models.Block(header, payload))
      case _ => None
    }
  }
}

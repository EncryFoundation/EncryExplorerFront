package controllers

import javax.inject.{Inject, _}
import models.{HistoryDao, Node}
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class NodeController @Inject()(cc: ControllerComponents,
                               historyDao: HistoryDao)
                              (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers {

  def nodes(): Action[AnyContent] = Action.async {
    historyDao.getAllNodes.map {
      case Nil => NotFound
      case node: List[Node] => Ok(views.html.nodeInfo(node))
    }
  }
}

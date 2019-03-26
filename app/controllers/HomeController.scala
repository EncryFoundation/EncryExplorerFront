package controllers

import javax.inject.{Inject, _}
import models.{Header, HistoryDao}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               historyDao: HistoryDao)
                              (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers {

  def index(): Action[AnyContent] = Action.async {
    historyDao.lastHeaders(50).map {
      case Nil => NotFound
      case list: List[Header] => Ok(views.html.index(list))
    }
  }

}

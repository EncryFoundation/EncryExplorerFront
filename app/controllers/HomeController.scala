package controllers

import io.circe.Json
import javax.inject.{Inject, _}
import io.circe.syntax._
import io.circe.generic.auto._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import models.{Header, HistoryDao}
import play.api.libs.circe.Circe
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               historyDao: HistoryDao)
                              (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers with Circe {

  def index(): Action[AnyContent] = Action.async {
    historyDao.lastHeaders(50).map {
      case Nil => NotFound
      case list: List[Header] => Ok(views.html.index(list))
    }
  }

  def listHeadersByCountApi(from: Int, count: Int): Action[AnyContent] = Action.async  {
    historyDao
      .listHeadersByCount(from, count)
      .map {
        case Nil => NotFound
        case list: List[Header] => Ok(list.asJson)
      }
  }

  def findHeaderApi(id: String): Action[AnyContent] = Action.async {
    historyDao
      .findHeader(id)
      .map {
        case Some(header) => Ok(header.asJson)
        case None => NotFound
      }
  }

  def listHeadersAtHeightApi(height: Int): Action[AnyContent] = Action.async {
    historyDao
      .listHeadersAtHeight(height)
      .map {
        case Nil => NotFound
        case list: List[Header] => Ok(list.asJson)
      }
  }

  def listLastHeadersApi(qty: Int): Action[AnyContent] = Action.async {
    historyDao
      .listLastHeadersByHeight(qty)
      .map {
        case Nil => NotFound
        case list: List[Header] => Ok(list.asJson)
      }
  }

  def listHeadersByHeightRangeApi(from: Int, to: Int): Action[AnyContent] = Action.async {
    historyDao
      .listHeadersByHeightRange(from, to)
      .map {
        case Nil => NotFound
        case list: List[Header] => Ok(list.asJson)
      }
  }


}

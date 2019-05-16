package controllers

import com.typesafe.scalalogging.StrictLogging
import javax.inject.{Inject, _}
import models.{Header, HistoryDao}
import play.api.mvc.{Action, _}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               historyDao: HistoryDao)
                              (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers with StrictLogging{

  def index(): Action[AnyContent] = Action.async {
    val headers: Future[List[Header]] = historyDao.lastHeaders()
    val height: Future[Option[Int]] = historyDao.lastHeights()

   val result: Future[(List[Header], Option[Int])] = for {
      headerOpt <- headers
      heightOpt <- height
    }yield (headerOpt, heightOpt)

    result.map{
      case (headerOpt,heightOpt) => Ok(views.html.index(headerOpt, heightOpt.get))
      case _ => NotFound
    }
  }

  def listHeadersByHeightRangeApi(to: Int): Action[AnyContent] = Action.async {
    val range = historyDao.listHeadersByHeightRange(to)
    val height = historyDao.lastHeights()

    val result = for{
      rangeOpt <- range
      heightOpt <- height
    }yield (rangeOpt, heightOpt)

    result.map{
      case (rangeOpt,heightOpt) => Ok(views.html.index(rangeOpt, heightOpt.get))
      case _ => NotFound
    }
  }
}

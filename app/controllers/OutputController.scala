package controllers

import javax.inject.{Inject, Singleton}
import models.TransactionsDao
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class OutputController @Inject()(cc: ControllerComponents,
                                 transactionsDao: TransactionsDao)(implicit ex: ExecutionContext)
  extends AbstractController(cc)  with Circe {

  def getOutput(id: String): Action[AnyContent] = Action.async {
    transactionsDao.outputById(id).map {
      case Some(output) => Ok(views.html.outputInfo(output))
      case None => NotFound
    }
  }

}

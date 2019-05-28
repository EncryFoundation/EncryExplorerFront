package controllers

import com.typesafe.scalalogging.StrictLogging
import javax.inject.Inject
import models._
import play.api.libs.circe.Circe

import scala.concurrent.ExecutionContext
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.mvc._

class WalletController @Inject()(cc: ControllerComponents,
                                 boxesDao: BoxesDao)
                                (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers with Circe with StrictLogging {

  def info(contractHash: String, from: Int, to: Int): Action[AnyContent] = Action.async {
    boxesDao.getBoxesByContractHash(contractHash, from: Int, to: Int).map {
      case Nil => NotFound
      case list: List[Output] => Ok(list.asJson)

    }
  }
}
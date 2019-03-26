package controllers

import javax.inject.Singleton
import models.HistoryDao
import play.api.mvc.{AbstractController, ControllerComponents, ControllerHelpers}
import scala.concurrent.ExecutionContext

//@Singleton
//class HistoryController(cc: ControllerComponents,
//                        historyDao: HistoryDao)
//                       (implicit ex: ExecutionContext) extends AbstractController(cc) with ControllerHelpers {
//
//
//}
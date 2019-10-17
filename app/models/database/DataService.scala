package models.database

import actors.ModifierMessages.{TransactionsA, TransactionsQ}
import akka.actor.ActorRef
import akka.util.Timeout
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import settings.ExplorerSettings
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class DataService @Inject()(config: Configuration, settings: ExplorerSettings, @Named("receiver") receiver: ActorRef, components: ControllerComponents) {
  implicit val timeout: Timeout = 5 seconds

  def getUncommittedTransactions: Future[List[models.DBTransaction]] =
    (receiver ? TransactionsQ()).mapTo[TransactionsA].map(_.txs)

}

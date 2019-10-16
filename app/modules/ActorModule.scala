package modules

import actors.ReceiverActor
import play.api.mvc._
import akka.actor._
import javax.inject._

@Singleton
class ActorModule @Inject()(system: ActorSystem, cc: ControllerComponents) extends AbstractController(cc) {
  //val receiver = system.actorOf(ReceiverActor.props, "receiver")
}
package modules

import actors.ReceiverActor
import play.api.mvc._
import akka.actor._
import javax.inject._

@Singleton
class Application @Inject()(system: ActorSystem, cc: ControllerComponents) extends AbstractController(cc) {

  println(s"actorSystem ${system.name} created")
  println("receiver created")
  val receiver = system.actorOf(Props(new ReceiverActor()), "receiver")

}
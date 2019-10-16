package modules
import actors.ReceiverActor
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ReceiverModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    bindActor[ReceiverActor]("receiver")
  }
}

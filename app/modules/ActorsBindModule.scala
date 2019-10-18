package modules
import actors.{CacheActor, ReceiverActor}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ActorsBindModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    bindActor[CacheActor]("cache")
    bindActor[ReceiverActor]("receiver")
  }
}

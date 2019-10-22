package modules
import actors.{TransStorage, Receiver}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ActorsBindModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    bindActor[TransStorage]("transStorage")
    bindActor[Receiver]("receiver")
  }
}

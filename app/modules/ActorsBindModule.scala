package modules
import actors.{Receiver, TransStorage}
import com.google.inject.AbstractModule
import javax.inject.Singleton
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport

@Singleton
class ActorsBindModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    Logger.info("ActorsBindModule")
    bindActor[TransStorage]("transStorage")
    bindActor[Receiver]("receiver")
  }
}

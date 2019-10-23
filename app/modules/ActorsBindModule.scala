package modules
import actors.{Receiver, TransStorage}
import com.google.inject.AbstractModule
import javax.inject.Singleton
import play.api.libs.concurrent.AkkaGuiceSupport

@Singleton
class ActorsBindModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    bindActor[TransStorage]("transStorage")
    bindActor[Receiver]("receiver")
  }
}
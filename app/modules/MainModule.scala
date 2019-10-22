package modules

import com.google.inject.{AbstractModule, Provides}
import com.typesafe.config.ConfigFactory
import play.api.{Configuration, Logger}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.codingwell.scalaguice.ScalaModule
import settings.ExplorerSettings

class MainModule extends AbstractModule with ScalaModule {

  @Provides
  def provideExplorerAppSettings(configuration: Configuration): ExplorerSettings = {
    Logger.info("MainModule")
    ConfigFactory.load("local.conf")
      .withFallback(configuration.underlying).as[ExplorerSettings]("encry")
  }
}

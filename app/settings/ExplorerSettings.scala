package settings

import scala.concurrent.duration.FiniteDuration

case class PostgresSettings(host: String, user: String, password: String)
case class CacheSettings(unconfirmedTransactionExpiredInterval: FiniteDuration)

case class ExplorerSettings(postgres: PostgresSettings, cache: CacheSettings)

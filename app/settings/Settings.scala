package settings

import scala.concurrent.duration.FiniteDuration

case class PostgresSettings(host: String, user: String, password: String)
case class TransSettings(unconfirmedTransactionExpiredInterval: FiniteDuration)

case class Settings(postgres: PostgresSettings, trans: TransSettings)

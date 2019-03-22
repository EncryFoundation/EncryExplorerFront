import sbt.Keys.version
import sbt._

name := "EncryExplorerFront"

version := "0.1"

organization := "org.encryfoundation"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq("Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  "Typesafe maven releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

val akkaHttpVersion = "10.1.3"
val circeVersion = "0.9.3"
val doobieVersion = "0.5.2"

val databaseDependencies = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion
)

val apiDependencies = Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.dripower" %% "play-circe" % "2609.1" exclude("io.circe", "*"),
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.1",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
)

val loggingDependencies = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

libraryDependencies += guice

libraryDependencies ++= (Seq(
  "net.codingwell" %% "scala-guice" % "4.2.1",
  "javax.xml.bind" % "jaxb-api" % "2.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.google.guava" % "guava" % "21.+",
  "com.iheart" %% "ficus" % "1.4.2",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.58",
  "org.whispersystems" % "curve25519-java" % "+",
  "org.rudogma" %% "supertagged" % "1.+",
  "org.scorexfoundation" %% "scrypto" % "2.1.1",
  "org.encry" %% "prism" % "0.2.3",
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",
) ++ databaseDependencies ++ apiDependencies ++ loggingDependencies)
  .map(_ exclude("ch.qos.logback", "*") exclude("ch.qos.logback", "*"))


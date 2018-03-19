name := """tascala"""
organization := "fscoward"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, ScalikejdbcPlugin)

scalaVersion := "2.12.3"

val circeVersion = "0.9.1"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  ws,
//  "com.google.oauth-client" % "google-oauth-client" % "1.23.0"
  "com.google.api-client" % "google-api-client" % "1.23.0",
  // circe
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "com.dripower" %% "play-circe" % "2609.0",
  "org.scalikejdbc" %% "scalikejdbc" % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2",
  "mysql" % "mysql-connector-java" % "6.0.6",

  "org.typelevel" %% "cats-core" % "1.0.1"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "fscoward.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "fscoward.binders._"

// for cats
scalacOptions += "-Ypartial-unification"

name := """tascala"""
organization := "fscoward"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

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
  "io.circe" %% "circe-generic-extras" % circeVersion
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "fscoward.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "fscoward.binders._"

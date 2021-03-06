libraryDependencies ++= Seq(
  "org.hsqldb" % "hsqldb" % "2.3.2",
  "mysql" % "mysql-connector-java" % "5.1.45"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.12")

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.2.1")

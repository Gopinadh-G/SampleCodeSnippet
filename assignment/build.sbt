name := """myworld"""
organization := "qvantel"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % "test"
libraryDependencies += ws
libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap" % "1.5.1-P27-B4"
)
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.18.5-play27"
libraryDependencies += "org.reactivemongo" %% "reactivemongo-play-json" % "0.18.5-play27"
libraryDependencies +=  "org.mockito" % "mockito-core" % "1.9.5"
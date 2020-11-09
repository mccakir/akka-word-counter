name := "akka-distributed-word-counter"

version := "0.1"

scalaVersion := "2.13.3"
val AkkaVersion = "2.6.10"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test
name := "selenium-scala"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalatest" %% "scalatest-selenium" % "3.0.0-SNAP13")

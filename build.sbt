name := "squerysis"

organization := "com.geishatokyo"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0", "2.9.1")

libraryDependencies ++= Seq(
  "org.squeryl" %% "squeryl" % "0.9.6-shard-SNAPSHOT",
  "mysql" % "mysql-connector-java" % "5.1.18" % "test",
  "junit" % "junit" % "4.8.1" % "test",
  "org.slf4j" % "slf4j-log4j12" % "1.6.3" % "provided"
)

libraryDependencies <+= (scalaVersion) {sv => sv match{
  case "2.9.1" | "2.9.1-1" => "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
  case "2.9.0" | "2.9.0-1" => "org.scala-tools.testing" %% "specs" % "1.6.8" % "test"
}}

//publishTo <<= version { v: String =>
//  if (v.trim.endsWith("SNAPSHOT"))
//    Some("sonatype-nexus-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
//  else
//    Some("sonatype-nexus-staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
//}

// vim:set ft=scala :

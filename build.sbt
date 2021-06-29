name := """building-portfolio"""
organization := "com.shinuki"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaJpa,
  javaWs,
  "com.h2database" % "h2" % "1.4.199",
  "org.hibernate" % "hibernate-core" % "5.5.3.Final",
  "org.postgresql" % "postgresql" % "42.2.5",
  "net.jodah" % "failsafe" % "2.4.0",
  "io.dropwizard.metrics" % "metrics-core" % "4.1.1",
  "com.palominolabs.http" % "url-builder" % "1.1.0",
)

javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-Werror"
)
// To use when we deploy the application
// PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"
lazy val root = (project in file(".")).settings(
  name := "16-bit-computer",
  version := "1.0.0",
  scalaVersion := "2.12.8",
  organization := "ditrapani.info",
  // Fork a new JVM for 'run' and 'test:run', to
  // avoid JavaFX double initialization problems
  fork := true
)

scalacOptions += "-target:jvm-1.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-Xlint",
  "-Ywarn-unused-import",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen"
)

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.181-R13"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

wartremoverWarnings ++= Warts.allBut(
  Wart.Equals,
  Wart.MutableDataStructures,
  Wart.NonUnitStatements,
  Wart.Nothing,
  Wart.Overloading
)

assemblyJarName in assembly := s"ljd-${name.value}-${version.value}.jar"

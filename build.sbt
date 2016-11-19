lazy val root = (project in file(".")).
  settings(
    name := "16-bit-computer",
    version := "0.1.0",
    scalaVersion := "2.12.0",
    organization := "ditrapani.info",
    // Fork a new JVM for 'run' and 'test:run', to
    // avoid JavaFX double initialization problems
    fork := true
  )

scalacOptions += "-target:jvm-1.8"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
wartremoverWarnings ++= Warts.unsafe

assemblyJarName in assembly := s"ljd-${name.value}-${version.value}.jar"

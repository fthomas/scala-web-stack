/// variables

val projectName = "funapp"
val rootPkg = "funstack"

val circeVersion = "0.6.1"
val http4sVersion = "0.15.2"
val logbackVersion = "1.1.8"
val scalaCheckVersion = "1.13.4"

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(server)
  .settings(commonSettings)
  .settings(noPublishSettings)

lazy val server = project
  .in(file("modules/server"))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(DebianPlugin, JavaServerAppPackaging, SystemVPlugin)
  .settings(moduleName := "server")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := rootPkg,
    javaOptions ++= Seq(
      "-Dapplication.configurationFile=src/universal/conf/application.conf",
      "-Dlogback.configurationFile=src/universal/conf/logback.xml"
    )
  )

/// settings

lazy val commonSettings = Def.settings(
  compileSettings,
  metadataSettings,
  packageSettings
)

lazy val compileSettings = Def.settings(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xfuture",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard"
  )
)

lazy val metadataSettings = Def.settings(
  name := projectName
)

lazy val packageSettings = Def.settings(
  maintainer := s"$projectName author <$projectName@example.com>"
)

lazy val noPublishSettings = Def.settings(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias("validate",
                 Seq(
                   "clean",
                   "scalafmtTest",
                   "coverageOn",
                   "test",
                   "coverageReport",
                   "coverageOff",
                   "doc",
                   "debian:packageBin"
                 ))

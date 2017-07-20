import sbtcrossproject.crossProject

/// variables

val projectName = "funapp"
val rootPkg = "funstack"

val circeVersion = "0.6.1"
val doobieVersion = "0.4.1"
val http4sVersion = "0.17.0-M3"
val logbackVersion = "1.2.3"
val refinedVersion = "0.8.2"
val scalaCheckVersion = "1.13.5"
val scalajsDomVersion = "0.9.3"

lazy val keyApplicationConf = settingKey[String](
  "System property that specifies the path of the configuration file.")

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(clientJS)
  .aggregate(commonJS)
  .aggregate(commonJVM)
  .aggregate(serverJVM)
  .settings(commonSettings)
  .settings(noPublishSettings)

lazy val client = crossProject(JSPlatform)
  .in(file("modules/client"))
  .dependsOn(common)
  .settings(moduleName := "client")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalajsDomVersion
    ),
    scalaJSUseMainModuleInitializer := true
  )

lazy val clientJS = client.js

lazy val common = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/common"))
  .settings(moduleName := "common")
  .settings(commonSettings)

lazy val commonJS = common.js
lazy val commonJVM = common.jvm

lazy val server = crossProject(JVMPlatform)
  .in(file("modules/server"))
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(DebianPlugin, JavaServerAppPackaging, SystemVPlugin)
  .settings(moduleName := "server")
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "eu.timepit" %% "refined" % refinedVersion,
      "eu.timepit" %% "refined-pureconfig" % refinedVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      //"org.tpolecat" %% "doobie-core-cats" % doobieVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
    ),
    keyApplicationConf := "application.conf",
    buildInfoKeys := Seq[BuildInfoKey](name, version, keyApplicationConf),
    buildInfoPackage := rootPkg,
    javaOptions ++= {
      val confDirectory = baseDirectory.value.absolutePath + "/src/universal/conf"
      Seq(
        s"-D${keyApplicationConf.value}=$confDirectory/application.conf",
        s"-Dlogback.configurationFile=$confDirectory/logback.xml"
      )
    }
  )

lazy val serverJVM = server.jvm

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
                   "package",
                   "packageSrc",
                   "debian:packageBin"
                 ))

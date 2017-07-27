import sbtcrossproject.{crossProject, CrossProject}

/// variables

val projectName = "typelevel-webapp"
val rootPkg = "org.example"

val circeVersion = "0.8.0"
val doobieVersion = "0.4.1"
val h2Version = "1.4.196"
val http4sVersion = "0.17.0-M3"
val logbackVersion = "1.2.3"
val refinedVersion = "0.8.2"
val scalaCheckVersion = "1.13.5"

lazy val keyApplicationConf = settingKey[String](
  "System property that specifies the path of the configuration file.")

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(clientJS)
  .aggregate(serverJVM)
  .aggregate(sharedJS)
  .aggregate(sharedJVM)
  .settings(commonSettings)
  .settings(noPublishSettings)

lazy val client = crossProject(JSPlatform)
  .configureCross(moduleCrossConfig("client"))
  .jsConfigure(_.dependsOn(sharedJS))
  .enablePlugins(ScalaJSWeb)
  .settings(
    scalaJSUseMainModuleInitializer := true
  )

lazy val clientJS = client.js

lazy val server = crossProject(JVMPlatform)
  .configureCross(moduleCrossConfig("server"))
  .jvmConfigure(_.dependsOn(sharedJVM))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(DebianPlugin, JavaServerAppPackaging, SystemVPlugin)
  .enablePlugins(SbtWeb)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "com.h2database" % "h2" % h2Version,
      "eu.timepit" %% "refined" % refinedVersion,
      "eu.timepit" %% "refined-pureconfig" % refinedVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.tpolecat" %% "doobie-core-cats" % doobieVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
    ),
    keyApplicationConf := "application.conf",
    javaOptions.in(reStart) ++= {
      val confDirectory = sourceDirectory.in(Universal).value / "conf"
      Seq(
        s"-D${keyApplicationConf.value}=$confDirectory/application.conf",
        s"-Dlogback.configurationFile=$confDirectory/logback.xml"
      )
    }
  )
  // sbt-buildinfo settings
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      moduleName,
      keyApplicationConf
    ),
    buildInfoPackage := rootPkg
  )
  // sbt-web-scalajs settings
  .settings(
    scalaJSProjects := Seq(clientJS),
    pipelineStages.in(Assets) := Seq(scalaJSPipeline)
  )

lazy val serverJVM = server.jvm

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("shared"))

lazy val sharedJS = shared.js
lazy val sharedJVM = shared.jvm

/// settings

def moduleCrossConfig(name: String): CrossProject => CrossProject =
  _.in(file(s"modules/$name"))
    .settings(moduleName := name)
    .settings(commonSettings)

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
  maintainer := s"$projectName author(s) <$projectName@example.org>"
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

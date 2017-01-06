/// variables

val http4sVersion = "0.16.0-SNAPSHOT"

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(server)
  .settings(noPublishSettings)
  .settings(
    name := "scala-web-stack",
    resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")
  )

lazy val server = project
  .in(file("modules/server"))
  .enablePlugins(BuildInfoPlugin)
  .settings(moduleName := "server")
  .settings(compileSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "funstack"
  )

/// settings

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
    "-Yno-predef",
    "-Yno-imports",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard"
  )
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
                 Seq("clean",
                     "scalafmtTest",
                     "coverageOn",
                     "test",
                     "coverageReport",
                     "coverageOff",
                     "doc"))

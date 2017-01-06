/// variables

val http4sVersion = "0.16.0-SNAPSHOT"

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(server)
  .settings(noPublishSettings)
  .settings(
    resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")
  )

lazy val server = project
  .in(file("modules/server"))
  .settings(moduleName := "server")
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion
    )
  )

/// settings

name := "scala-web-stack"

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

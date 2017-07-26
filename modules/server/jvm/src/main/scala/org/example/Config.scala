package org.example

import eu.timepit.refined.auto._
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import fs2.Task
import java.nio.file.{Path, Paths}
import org.log4s.getLogger
import scala.util.Properties

final case class Config(
    httpHost: NonEmptyString = "::",
    httpPort: PortNumber = 8080
)

object Config {
  private[this] val logger = getLogger

  def propAsPath(name: String): Task[Option[Path]] =
    Task.delay(Properties.propOrNone(name).map(Paths.get(_)))

  def load: Task[Config] = {
    val prop = BuildInfo.keyApplicationConf
    propAsPath(prop).flatMap {
      case None =>
        logger.info(
          s"Using default configuration (property '$prop' is not set)")
        Task.now(Config())

      case Some(path) =>
        logger.info(s"Loading configuration from $path")
        Task.delay(pureconfig.loadConfigOrThrow[Config](path))
    }
  }
}

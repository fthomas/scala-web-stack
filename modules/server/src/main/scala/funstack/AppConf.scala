package funstack

import eu.timepit.refined.auto._
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import java.nio.file.{Path, Paths}
import pureconfig.loadConfig
import scala.util.Properties
import scalaz.concurrent.Task

final case class AppConf(
    httpHost: String = "::",
    httpPort: PortNumber = 8080
)

object AppConf {
  private[this] val logger = org.log4s.getLogger

  def confFile: Task[Option[Path]] =
    Task.delay(
      Properties
        .propOrNone(BuildInfo.keyApplicationConf)
        .map(Paths.get(_)))

  def loadConf: Task[AppConf] =
    confFile.flatMap {
      case None =>
        logger.info(
          "Using default configuration " +
            s"(property ${BuildInfo.keyApplicationConf} is not set)")
        Task.now(AppConf())

      case Some(path) =>
        logger.info(s"Loading configuration from $path")
        loadConfig[AppConf](path).fold(Task.fail, Task.now)
    }
}

package funstack

import java.lang.String
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scala.Int
import scalaz.concurrent.Task

object Blaze {
  final case class FunAppSettings(
      httpHost: String = "::",
      httpPort: Int = 8080
  )

  val settings = FunAppSettings()
  val server: Task[Server] = BlazeBuilder
    .bindHttp(settings.httpPort, settings.httpHost)
    .mountService(Service.route)
    .start
}

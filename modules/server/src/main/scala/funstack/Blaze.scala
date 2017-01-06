package funstack

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scalaz.concurrent.Task

object Blaze {
  final case class FunAppConf(
      httpHost: String = "::",
      httpPort: Int = 8080
  )

  val conf = FunAppConf()
  val server: Task[Server] = BlazeBuilder
    .bindHttp(conf.httpPort, conf.httpHost)
    .mountService(Service.route)
    .start
}

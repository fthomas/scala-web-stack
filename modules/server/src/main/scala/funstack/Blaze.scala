package funstack

import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.PortNumber
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scalaz.concurrent.Task

object Blaze {
  final case class FunAppConf(
      httpHost: String = "::",
      httpPort: PortNumber = 8080
  )

  val conf = FunAppConf()
  val server: Task[Server] = BlazeBuilder
    .bindHttp(conf.httpPort, conf.httpHost)
    .mountService(Service.route)
    .start
}

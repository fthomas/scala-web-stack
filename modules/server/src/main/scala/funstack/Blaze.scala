package funstack

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scalaz.concurrent.Task

object Blaze {
  val defaultHost = "::"
  val defaultPort = 8080

  val server: Task[Server] = BlazeBuilder
    .bindHttp(defaultPort, defaultHost)
    .mountService(Service.route)
    .start
}

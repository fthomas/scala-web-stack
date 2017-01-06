package funstack

import java.lang.String
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scala.Int
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

package funstack

import eu.timepit.refined.auto._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scalaz.concurrent.Task

object Blaze {
  val server: Task[Server] =
    AppConf.loadConf.flatMap { conf =>
      BlazeBuilder
        .bindHttp(conf.httpPort, conf.httpHost)
        .mountService(Service.route)
        .start
    }
}

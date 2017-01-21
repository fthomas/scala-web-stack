package funstack

import eu.timepit.refined.auto._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.{Server, ServerApp}
import scala.collection.immutable.List
import scalaz.concurrent.Task

object FunApp extends ServerApp {
  override def server(args: List[String]): Task[Server] =
    AppConf.loadConf.flatMap { conf =>
      BlazeBuilder
        .bindHttp(conf.httpPort, conf.httpHost)
        .mountService(Service.route)
        .start
    }
}

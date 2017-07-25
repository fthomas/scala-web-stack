package funstack

import eu.timepit.refined.auto._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

object FunApp extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(Config.loadConfig).flatMap { conf =>
      BlazeBuilder
        .bindHttp(conf.httpPort, conf.httpHost)
        .mountService(Service.api, "/api")
        .mountService(Service.assets, "/assets")
        .serve
    }
}

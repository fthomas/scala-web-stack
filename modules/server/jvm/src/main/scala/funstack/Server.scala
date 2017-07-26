package funstack

import eu.timepit.refined.auto._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

object Server extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(Config.load).flatMap { config =>
      BlazeBuilder
        .bindHttp(config.httpPort, config.httpHost)
        .mountService(Service.api, "/api")
        .mountService(Service.assets, "/assets")
        .serve
    }
}

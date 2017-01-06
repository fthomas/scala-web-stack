package funstack

import org.http4s.server.{Server, ServerApp}
import scala.collection.immutable.List
import scalaz.concurrent.Task

object FunApp extends ServerApp {
  override def server(args: List[String]): Task[Server] =
    Blaze.server
}

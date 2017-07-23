package funstack

import fs2.Task
import fs2.io.readInputStream
import io.circe.syntax._
import org.http4s.{HttpService, Response}
import org.http4s.circe.jsonEncoder
import org.http4s.dsl._

object Service {
  val route = HttpService {
    case req @ GET -> Root / "assets" / name if name.nonEmpty =>
      getResourceAsResponse(req.pathInfo)

    case GET -> Root / "version" =>
      Ok(BuildInfo.version)

    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }

  def getResourceAsResponse(name: String): Task[Response] =
    getClass.getResourceAsStream(name) match {
      case null => NotFound()
      case is => Ok(readInputStream(Task.now(is), chunkSize = 4096))
    }
}

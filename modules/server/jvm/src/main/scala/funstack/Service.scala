package funstack

import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.circe.jsonEncoder
import org.http4s.dsl._

object Service {
  val route = HttpService {
    case GET -> Root / "version" =>
      Ok(BuildInfo.version)

    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }
}

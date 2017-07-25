package funstack

import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.circe.jsonEncoder
import org.http4s.dsl._
import org.http4s.server.staticcontent.WebjarService.Config
import org.http4s.server.staticcontent.webjarService

object Service {
  val api = HttpService {
    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(Config())
}

package funstack

import io.circe.generic.auto._
import io.circe.syntax._
import java.lang.String
import org.http4s.HttpService
import org.http4s.circe.jsonEncoder
import org.http4s.dsl._

object Service {
  val route = HttpService {
    case GET -> Root / "version" => Ok(BuildInfo.version)

    case GET -> Root / "info.json" =>
      final case class Info(name: String, version: String)
      val payload = Info(BuildInfo.name, BuildInfo.version).asJson
      Ok(payload)
  }
}

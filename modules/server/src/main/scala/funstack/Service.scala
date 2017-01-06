package funstack

import org.http4s.HttpService
import org.http4s.dsl._

object Service {
  val route = HttpService {
    case GET -> Root / "version" => Ok(BuildInfo.version)
  }
}

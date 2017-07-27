package org.example

import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.circe.jsonEncoder
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Service {
  val api = HttpService {
    case GET -> Root / "now.json" =>
      Ok(Storage.now.map(_.asJson))

    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}

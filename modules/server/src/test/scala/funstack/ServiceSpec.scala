package funstack

import org.http4s.{MediaType, Request, Uri}
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.Predef.=:=._
import scala.Some

object ServiceSpec extends Properties("Service") {

  property("MediaType of /info.json") = secure {
    val response =
      Service.route.run(Request(uri = Uri(path = "info.json"))).run
    response.contentType.map(_.mediaType) ?= Some(MediaType.`application/json`)
  }
}

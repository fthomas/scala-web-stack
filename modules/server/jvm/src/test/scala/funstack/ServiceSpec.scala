package funstack

import org.http4s.{MediaType, Request, Uri}
import org.scalacheck.Prop._
import org.scalacheck.Properties

object ServiceSpec extends Properties("Service") {

  property("body of /version is not empty") = secure {
    val request: Request = Request(uri = Uri(path = "version"))
    val response = Service.route.run(request).unsafeRun().orNotFound
    val body = response.bodyAsText.runLog.unsafeRun().mkString
    body.nonEmpty
  }

  property("MediaType of /version.json") = secure {
    val request: Request = Request(uri = Uri(path = "version.json"))
    val response = Service.route.run(request).unsafeRun().orNotFound
    response.contentType.map(_.mediaType) ?= Some(MediaType.`application/json`)
  }
}

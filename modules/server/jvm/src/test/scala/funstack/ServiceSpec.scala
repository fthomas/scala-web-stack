package funstack

import org.http4s.{MediaType, Request, Response, Uri}
import org.scalacheck.Prop._
import org.scalacheck.Properties

object ServiceSpec extends Properties("Service") {

  property("body of /assets/client-opt.js contains 'Hello, world!'") = secure {
    val request: Request = Request(uri = Uri(path = "/assets/client-opt.js"))
    val response = unsafeGetResponse(request)
    val body = unsafeBodyAsText(response)
    body.contains("Hello, world!")
  }

  property("body of /version is not empty") = secure {
    val request: Request = Request(uri = Uri(path = "/version"))
    val response = unsafeGetResponse(request)
    val body = unsafeBodyAsText(response)
    body.nonEmpty
  }

  property("MediaType of /version.json") = secure {
    val request: Request = Request(uri = Uri(path = "/version.json"))
    val response = unsafeGetResponse(request)
    response.contentType.map(_.mediaType) ?= Some(MediaType.`application/json`)
  }

  def unsafeGetResponse(request: Request): Response =
    Service.route.run(request).unsafeRun().orNotFound

  def unsafeBodyAsText(response: Response): String =
    response.bodyAsText.runLog.unsafeRun().mkString
}

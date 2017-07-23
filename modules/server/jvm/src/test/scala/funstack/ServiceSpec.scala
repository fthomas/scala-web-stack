package funstack

import org.http4s._
import org.scalacheck.Prop._
import org.scalacheck.{Prop, Properties}

object ServiceSpec extends Properties("Service") {

  property("body of /assets/client-opt.js contains 'Hello, world!'") = secure {
    val request: Request = Request(uri = Uri(path = "/assets/client-opt.js"))
    val response = unsafeGetResponse(request)
    val body = unsafeBodyAsText(response)
    body.contains("Hello, world!")
  }

  property("/assets/foo.js returns 404") = secure {
    val request: Request = Request(uri = Uri(path = "/assets/foo.js"))
    val response = Service.route.run(request).unsafeRun()
    response.cata(_.status ?= Status.NotFound, Prop.falsified)
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

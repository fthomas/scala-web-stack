package funstack

import org.http4s._
import org.scalacheck.Prop._
import org.scalacheck.{Prop, Properties}

object ServiceSpec extends Properties("Service") {

  property("MediaType of /version.json") = secure {
    val request = Request(Method.GET, Uri.uri("/version.json"))
    val response = unsafeGetResponse(Service.api, request)
    mediaTypeEquals(response, MediaType.`application/json`)
  }

  property("body of client-opt.js contains 'Hello, world!'") = secure {
    val path = s"/${BuildInfo.moduleName}/${BuildInfo.version}/client-opt.js"
    val request = Request(Method.GET, Uri(path = path))
    val response = unsafeGetResponse(Service.assets, request)
    val body = unsafeBodyAsText(response)
    body.contains("Hello, world!")
  }

  def mediaTypeEquals(response: Response, mediaType: MediaType): Prop =
    response.contentType.map(_.mediaType) ?= Some(mediaType)

  def unsafeGetResponse(service: HttpService, request: Request): Response =
    service.run(request).unsafeRun().orNotFound

  def unsafeBodyAsText(response: Response): String =
    response.bodyAsText.runLog.unsafeRun().mkString
}

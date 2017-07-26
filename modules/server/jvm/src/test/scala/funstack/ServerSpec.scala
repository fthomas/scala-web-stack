package funstack

import org.scalacheck.Prop._
import org.scalacheck.Properties

object ServerSpec extends Properties("Server") {

  property("stream") = secure {
    Server.stream(List.empty)
    true
  }
}

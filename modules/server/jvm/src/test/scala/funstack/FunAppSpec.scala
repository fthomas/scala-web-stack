package funstack

import org.scalacheck.Prop._
import org.scalacheck.Properties

object FunAppSpec extends Properties("FunApp") {

  property("stream") = secure {
    FunApp.stream(List.empty)
    true
  }
}

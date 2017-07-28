package org.example

import java.io.FileNotFoundException
import org.specs2.mutable.Specification
import scala.util.Properties

object ConfigSpec extends Specification {
  "Config.load(prop) must" >> {
    "return the default configuration if prop is not set" >> {
      val prop = "app.conf.1"
      Properties.clearProp(prop)
      Config.load(prop).unsafeRun() must_=== Config()
    }
    "fail if prop references a non-existent file" >> {
      val prop = "app.conf.2"
      Properties.setProp(prop, "non-existent.conf")
      Config.load(prop).unsafeRun() must throwA[FileNotFoundException]
    }
  }
}

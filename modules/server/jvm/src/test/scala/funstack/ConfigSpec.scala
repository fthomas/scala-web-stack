package funstack

import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.util.Properties

object ConfigSpec extends Properties("Config") {

  property("loadConfig") =
    secure {
      Properties.clearProp(BuildInfo.keyApplicationConf)
      "default" |: (Config.loadConfig.unsafeRun() ?= Config())
    } && secure {
      Properties.setProp(BuildInfo.keyApplicationConf, "non-existent.conf")
      "non-existent" |: Config.loadConfig.unsafeAttemptRun().isLeft
    }
}

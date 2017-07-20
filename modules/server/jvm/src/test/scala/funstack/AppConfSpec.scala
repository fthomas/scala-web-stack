package funstack

import funstack.BuildInfo.keyApplicationConf
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.util.Properties

object AppConfSpec extends Properties("AppConf") {

  property("confFile is empty if application.conf is not set") = secure {
    Properties.clearProp(keyApplicationConf)
    AppConf.confFile.unsafeRun().isEmpty
  }

  property("loadConf returns default AppConf if application.conf is not set") =
    secure {
      Properties.clearProp(keyApplicationConf)
      AppConf.loadConf.unsafeRun() ?= AppConf()
    }
}

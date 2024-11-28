package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import vc.Feeders.users

object WebtoursScenario {
  def apply(): ScenarioBuilder = new WebtoursScenario().scn
}

class WebtoursScenario {
  val loginGroup: ChainBuilder = group("OpenHomePageAndLogin") {
    exec(Actions.openWelcomePage)
  }

  val scn: ScenarioBuilder = scenario("Webtours scenario")
    .feed(users)
    .exec(loginGroup)
}

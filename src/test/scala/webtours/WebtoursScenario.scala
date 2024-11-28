package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import webtours.Feeders.users

object WebtoursScenario {
  def apply(): ScenarioBuilder = new WebtoursScenario().scn
}

class WebtoursScenario {
  val loginGroup: ChainBuilder = group("OpenHomePageAndLogin") {
    exec(Actions.openWelcomePage)
      .exec(Actions.getSessionResult)
      .exec(Actions.loginPostResult)
      .exec(Actions.homeNavigationPageResult)
      .exec(Actions.loginGetResult)
  }

  val scn: ScenarioBuilder = scenario("Webtours scenario")
    .feed(users)
    .exec(loginGroup)
}

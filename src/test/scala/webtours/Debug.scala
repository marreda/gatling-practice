package webtours

import io.gatling.core.Predef._

class Debug extends Simulation {
  setUp(WebtoursScenario().inject(atOnceUsers(1)))
    .protocols(httpProtocol)
//    .assertions(global.responseTime.max.lt(1000))
//    .maxDuration(1000)
}

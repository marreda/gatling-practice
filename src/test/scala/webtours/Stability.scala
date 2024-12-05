package webtours

import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class Stability extends Simulation {
  val intensity = 2

  setUp(WebtoursScenario().inject(
    // разгон
    rampUsersPerSec(0) to intensity during 10.seconds,
    // полка
    constantUsersPerSec(intensity) during 60.minute
  ))
    .protocols(httpProtocol)
    .maxDuration(3600)
    .assertions(forAll.failedRequests.percent.lte(5))
    .assertions(details("OpenHomePageAndLogin" / "loginPostResult").failedRequests.percent.is(0))
    .assertions(details("BuyTicket" / "buyTicketResult").failedRequests.percent.is(0))
}

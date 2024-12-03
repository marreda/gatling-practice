package webtours

import io.gatling.core.Predef._

class MaxPerformance extends Simulation {
  setUp(WebtoursScenario().inject(
    // интенсивность на ступень
    incrementUsersPerSec(1.0)
      // Количество ступеней
      .times(5)
      // Длительность полки
      .eachLevelLasting(60)
      // Длительность разгона
      .separatedByRampsLasting(10)
      // Начало нагрузки с
      .startingFrom(0)
  ))
    .protocols(httpProtocol)
    .maxDuration(360)
    .assertions(forAll.failedRequests.percent.lte(5))
    .assertions(details("OpenHomePageAndLogin" / "loginPostResult").failedRequests.percent.is(0))
    .assertions(details("BuyTicket" / "buyTicketResult").failedRequests.percent.is(0))
}

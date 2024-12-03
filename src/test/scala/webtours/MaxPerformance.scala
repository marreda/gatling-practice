package webtours

import io.gatling.core.Predef._

class MaxPerformance extends Simulation {
  val intensity = 5
  val stagesNumber = 5
  val stageDuration = 60
  val rampDuration = 10
  val startingRate = 0
  val maxDuration = 300

  setUp(WebtoursScenario().inject(
    // интенсивность на ступень
    incrementUsersPerSec(intensity / stagesNumber)
      // Количество ступеней
      .times(stagesNumber)
      // Длительность полки
      .eachLevelLasting(stageDuration)
      // Длительность разгона
      .separatedByRampsLasting(rampDuration)
      // Начало нагрузки с
      .startingFrom(startingRate),
//    constantUsersPerSec(5.0).during(2.minutes)
  ))
    .protocols(httpProtocol)
    .maxDuration(maxDuration)
}

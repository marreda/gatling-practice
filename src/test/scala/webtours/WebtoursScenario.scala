package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import webtours.Feeders.users

import scala.util.Random

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

  val chooseDirectionGroup: ChainBuilder = group("ChooseDirectionAndFindFlight") {
    exec(Actions.searchPageResult)
      .exec(Actions.flightNavigationPageResult)
      .exec(Actions.welcomeReservationsPageResult)
      .exec(session => {
        val departureCities = session("departureCities").as[Vector[String]]
        var arrivalCities = session("arrivalCities").as[Vector[String]]

        val indexDepart = Random.nextInt(departureCities.size)
        val departureCity = departureCities(indexDepart)
        println("depart = " + departureCity)

        arrivalCities = arrivalCities.filter(_ != departureCity)
        val indexArrival = Random.nextInt(arrivalCities.size)
        val arrivalCity = arrivalCities(indexArrival)
        println("arrive = " + arrivalCity)

        val newSession = session.setAll(Map("depart" -> departureCity, "arrive" -> arrivalCity))
        newSession
      })
  }

  val findFlightGroup: ChainBuilder = group("findFlightGroup") {
    exec(Actions.flightReservationsResult)
      .exec(session => {
        val outboundFlights = session("outboundFlights").as[Vector[String]]

        val indexDepart = Random.nextInt(outboundFlights.size)
        val outboundFlight = outboundFlights(indexDepart)
        println("outboundFlight = " + outboundFlight)

        val newSession = session.set("outboundFlight", outboundFlight)
        newSession
      })
  }

  val scn: ScenarioBuilder = scenario("Webtours scenario")
    .feed(users)
    .exec(loginGroup)
    .exec(chooseDirectionGroup)
    .exec(findFlightGroup)
}

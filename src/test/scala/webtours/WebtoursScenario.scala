package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure._
import webtours.Feeders.users

import java.time.Year
import java.util.UUID
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

  val chooseDirectionGroup: ChainBuilder = group("ChooseDirection") {
    exec(Actions.searchPageResult)
      .exec(Actions.flightNavigationPageResult)
      .exec(Actions.welcomeReservationsPageResult)
      .exec(session => {
        val departureCities = session("departureCities").as[Vector[String]]
        var arrivalCities = session("arrivalCities").as[Vector[String]]

        val indexDepart = Random.nextInt(departureCities.size)
        val departureCity = departureCities(indexDepart)

        arrivalCities = arrivalCities.filter(_ != departureCity)
        val indexArrival = Random.nextInt(arrivalCities.size)
        val arrivalCity = arrivalCities(indexArrival)

        val newSession = session.setAll(Map("depart" -> departureCity, "arrive" -> arrivalCity))
        newSession
      })
  }

  val findFlightGroup: ChainBuilder = group("FindFlight") {
    exec(Actions.flightReservationsResult)
      .exec(session => {
        val outboundFlights = session("outboundFlights").as[Vector[String]]

        val indexDepart = Random.nextInt(outboundFlights.size)
        val outboundFlight = outboundFlights(indexDepart)

        val newSession = session.set("outboundFlight", outboundFlight)
        newSession
      })
  }

  val buyTicketGroup: ChainBuilder = group("BuyTicket") {
    exec(Actions.paymentDetailsResult)
      .exec(session => {
        val creditCard = UUID.randomUUID.toString
        val nextYear = Year.now.getValue + 1
        val newSession = session.setAll(Map("creditCard" -> creditCard, "expDate" -> nextYear))
        newSession
      })
      .exec(Actions.buyTicketResult)
  }

  val openHomePageGroup: ChainBuilder = group("OpenHomePage") {
    exec(Actions.openHomePage)
      .exec(Actions.homeNavigationPageResult)
      .exec(Actions.loginGetResult)
  }

  val scn: ScenarioBuilder = scenario("Webtours scenario")
    .feed(users)
    .exec(loginGroup)
    .exec(chooseDirectionGroup)
    .exec(findFlightGroup)
    .exec(buyTicketGroup)
    .exec(openHomePageGroup)
}

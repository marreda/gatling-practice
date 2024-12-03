package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object Actions {
  val openWelcomePage: HttpRequestBuilder = http("openWelcomePage")
    .get("/welcome.pl?signOff=true")
    .check(status is 200)
    .check(regex("Web Tours").exists)

  val getSessionResult: HttpRequestBuilder = http("getSessionResult")
    .get("/nav.pl?in=home")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .check(status is 200)
    .check(regex("Web Tours Navigation Bar").exists)
    .check(css("[name=userSession]", "value").saveAs("sessionValue"))

  val loginPostResult: HttpRequestBuilder = http("loginPostResult")
    .post("/login.pl")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .formParam("userSession", "#{sessionValue}")
    .formParam("username", "#{login}")
    .formParam("password", "#{password}")
    .check(status is 200)
    .check(regex("Web Tours").exists)

  val homeNavigationPageResult: HttpRequestBuilder = http("homeNavigationPageResult")
    .get("/nav.pl?page=menu&in=home")
    .check(status is 200)
    .check(regex("Web Tours Navigation Bar").exists)

  val loginGetResult: HttpRequestBuilder = http("loginGetResult")
    .get("/login.pl?intro=true")
    .check(status is 200)
    .check(regex("Welcome to Web Tours").exists)

  val searchPageResult: HttpRequestBuilder = http("searchPageResult")
    .get("/welcome.pl?page=search")
    .check(status is 200)

  val flightNavigationPageResult: HttpRequestBuilder = http("flightNavigationPageResult")
    .get("/nav.pl?page=menu&in=flights")
    .check(status is 200)

  val welcomeReservationsPageResult: HttpRequestBuilder = http("welcomeReservationsPageResult")
    .get("/reservations.pl?page=welcome")
    .check(status is 200)
    .check(css("table select[name=depart] option", "value").findAll.saveAs("departureCities"))
    .check(css("table select[name=arrive] option", "value").findAll.saveAs("arrivalCities"))
    .check(css("input[name=advanceDiscount]", "value").saveAs("advanceDiscount"))
    .check(css("input[name=departDate]", "value").saveAs("departDate"))
    .check(css("input[name=returnDate]", "value").saveAs("returnDate"))
    .check(css("input[name=numPassengers]", "value").saveAs("numPassengers"))
    .check(css("input[name=seatPref][checked=checked]", "value").saveAs("seatPref"))
    .check(css("input[name=seatType][checked=checked]", "value").saveAs("seatType"))

  val flightReservationsResult: HttpRequestBuilder = http("flightReservationsResult")
    .post("/reservations.pl")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .formParam("advanceDiscount", "#{advanceDiscount}")
    .formParam("depart", session => session("depart").as[String])
    .formParam("departDate", "#{departDate}")
    .formParam("arrive", session => session("arrive").as[String])
    .formParam("returnDate", "#{returnDate}")
    .formParam("numPassengers", "#{numPassengers}")
    .formParam("seatPref", "#{seatPref}")
    .formParam("seatType", "#{seatType}")
    .formParam("findFlights.x", 46)
    .formParam("findFlights.y", 2)
    .check(status is 200)
    .check(regex("Flight Selections").exists)
    .check(css("input[name=outboundFlight]", "value").findAll.saveAs("outboundFlights"))
}

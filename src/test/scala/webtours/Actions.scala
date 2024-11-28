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
    .check(regex("input[name=userSession]="))

  val loginPostResult: HttpRequestBuilder = http("loginPostResult")
    .post("/login.pl")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .check(status is 200)
    .check(regex("Web Tours Navigation Bar").exists)
}

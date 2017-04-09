package com.idugalic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://127.0.0.1:8080")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:52.0) Gecko/20100101 Firefox/52.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("RecordedSimulation")
		.exec(http("GET - Blog Posts")
			.get("/blogposts")
			.headers(headers_0))
		.pause(0)
		.exec(http("GET Projects")
			.get("/projects")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
}
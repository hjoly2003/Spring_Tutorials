package com.proto.gtway;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

/**
 * [N]:wiremock - Takes advantage of {@code WireMock} from Spring Cloud Contract to start up a server that can mock the APIs from {@code GreetingServiceApplication}.<p/>
 * {@code @SpringBootTest} - [N]:config-props - Takes advantage of our {@code UriConfiguration} class and set the {@code greetingUri} property to the {@code WireMock} server running locally.
 * @author Ryan Baxter
 */
// tag::code[]
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"greetingUri=http://localhost:${wiremock.server.port}"})
@AutoConfigureWireMock(port = 0)	// This annotation starts WireMock on a random port.
public class GatewayServiceApplicationTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void contextLoads() throws Exception {

		// Setups “stubs” for the GreetingServiceApplication APIs we call through the Gateway and mock the behavior we expect.

		stubFor(get(urlEqualTo("/hello"))
				.willReturn(aResponse()
					.withBody("{\"headers\":{\"Hello\":\"World\"}}")
					.withHeader("Content-Type", "application/json")));
		
		// stubFor(get(urlEqualTo("/delay/3"))
		// 	.willReturn(aResponse()
		// 		.withBody("no fallback")
		// 		.withFixedDelay(3000)));

		// Uses WebTestClient to make requests to the Gateway and validate the responses.
		
		webClient
			.get().uri("/hello")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.headers.Hello").isEqualTo("World");

		// webClient
		// 	.get().uri("/delay/3")
		// 	.header("Host", "www.circuitbreaker.com")
		// 	.exchange()
		// 	.expectStatus().isOk()
		// 	.expectBody()
		// 	.consumeWith(
		// 		response -> assertThat(response.getResponseBody()).isEqualTo("fallback".getBytes()));
	}
}
// end::code[]
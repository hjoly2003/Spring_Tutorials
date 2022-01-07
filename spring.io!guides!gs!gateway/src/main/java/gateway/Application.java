package gateway;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tag::code[]
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// tag::route-locator[]
	/**
	 * The {@code RouteLocator} is used by the Gateway Handler to resolve route configurations.
	 */
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {

		String httpUri = uriConfiguration.getHttpbin();

		return builder.routes()
			// [N]:gtway - Filter that adds the "hello" request header with value "World"
			// Test with: curl http://localhost:8080/get
			.route(p -> p
				.path("/get") // If the path is a "get"... (a Java 8' Predicate)
				.filters(f -> f.addRequestHeader("Hello", "World"))	// A standard Spring's WebFilter
				.uri(httpUri)) // Redirected to this route ("http://httpbin.org:80")
			
			// [N]:circuit - Wrap the route in a circuit breaker. 
			// Test with: curl --dump-header - --header 'Host: www.circuitbreaker.com' http://localhost:8080/delay/3
			.route(p -> p
				.host("*.circuitbreaker.com") // If the host is "*.circuitbreaker.com"...
				.filters(f -> f
					.circuitBreaker(config -> config
						.setName("mycmd")
						.setFallbackUri("forward:/fallback")))
				.uri(httpUri))
			.build();
	}
	// end::route-locator[]

	// tag::fallback[]
	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
	// end::fallback[]
}

// tag::uri-configuration[]
/**
 * [N]:config-props
 */
@ConfigurationProperties
class UriConfiguration {
	
	private String httpbin = "http://httpbin.org:80";

	public String getHttpbin() {
		return httpbin;
	}

	public void setHttpbin(String httpbin) {
		this.httpbin = httpbin;
	}
}
// end::uri-configuration[]
// end::code[]
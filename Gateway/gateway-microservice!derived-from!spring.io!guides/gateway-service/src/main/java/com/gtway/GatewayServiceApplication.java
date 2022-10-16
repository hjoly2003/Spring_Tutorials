package com.proto.gtway;

import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.cloud.client.ServiceInstance;
// import org.springframework.cloud.client.discovery.DiscoveryClient;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tag::code[]
/**
 * A client that both registers itself with the registry and uses the Spring Cloud DiscoveryClient abstraction to interrogate the registry for its own host and port.
 * [N]:eureka-client - We need the {@code @EnableEurekaClient} annotation to use the Eureka Client library.
 * [N]:eureka-client]:discovery - We need the {@code @EnableDiscoveryClient} annotation to use the Discovery Client library.
 */
@SpringBootApplication
// @EnableEurekaClient // [N]
// @EnableDiscoveryClient // [N]
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

    // // TODO [?] Required?
	// /** [N]:eureka-client]:discovery - Made available by Spring Context thanks to the {@code @EnableDiscoveryClient} annotation */
	// @Autowired
	// private DiscoveryClient discoveryClient;

    // // TODO [?] Required?
	// /**
	//  * @param applicationName
	//  * @return [N]:eureka-client]:discovery - a list of all the instances of the application service. The ServiceInstance class holds information about a specific instance of a service, including its hostname, port, and URI.
	//  */
	// @RequestMapping("/service-instances/{applicationName}")
	// public List<ServiceInstance> serviceInstancesByApplicationName(
	// 		@PathVariable String applicationName) {
	// 	return this.discoveryClient.getInstances(applicationName);
	// }

	// tag::route-locator[]
	/**
	 * The {@code RouteLocator} is used by the Gateway Handler to resolve route configurations.
	 */
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {

		String httpUri = uriConfiguration.getGreetingUri();

		return builder.routes()
			// [N]:gtway - Filter that adds the "hello" request header with value "World"
			// Test with: curl http://localhost:8072/hello
			.route(p -> p
				.path("/hello") // If the path is a "hello"... (a Java 8' Predicate)
				.filters(f -> f.addRequestHeader("Hello", "World"))	// A standard Spring's WebFilter
				.uri(httpUri)) // Redirected to this route ("http://localhost:8081/hello") //("lb://greeting_service")
			
			// [N]:circuit - Wrap the route in a circuit breaker. 
			// Test with: curl --dump-header - --header 'Host: www.circuitbreaker.com' http://localhost:8072/delay/3
			// .route(p -> p
			// 	.host("*.circuitbreaker.com") // If the host is "*.circuitbreaker.com"...
			// 	.filters(f -> f
			// 		.circuitBreaker(config -> config
			// 			.setName("mycmd")
			// 			.setFallbackUri("forward:/fallback")))
			// 	.uri(httpUri))
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
	
	// TODO [?]
	private String greeting_url = "https://httpbin.org:80"; //"http://localhost:8081"; //"lb://greeting-service";

	public String getGreetingUri() {
		return greeting_url;
	}

	public void setGreetingUri(String greeting_url) {
		this.greeting_url = greeting_url;
	}
}
// end::uri-configuration[]
// end::code[]
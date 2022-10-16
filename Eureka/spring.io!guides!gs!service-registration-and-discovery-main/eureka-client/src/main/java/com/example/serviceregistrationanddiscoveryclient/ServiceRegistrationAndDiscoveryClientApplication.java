package com.example.serviceregistrationanddiscoveryclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A client that both registers itself with the registry and uses the Spring Cloud DiscoveryClient abstraction to interrogate the registry for its own host and port.
 * [N]:eureka-client - We need the {@code @EnableEurekaClient} annotation to use the Eureka Client library.
 * [N]:eureka-client]:discovery - We need the {@code @EnableDiscoveryClient} annotation to use the Discovery Client library.
 */
@SpringBootApplication
@EnableEurekaClient // [N]
@EnableDiscoveryClient // [N]
public class ServiceRegistrationAndDiscoveryClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationAndDiscoveryClientApplication.class, args);
	}
}

@RestController
class ServiceInstanceRestController {

    /** [N]:eureka-client]:discovery - Made available by Spring Context thanks to the {@code @EnableDiscoveryClient} annotation */
	@Autowired
	private DiscoveryClient discoveryClient;

	/**
	 * @param applicationName
	 * @return [N]:eureka-client]:discovery - a list of all the instances of the application service. The ServiceInstance class holds information about a specific instance of a service, including its hostname, port, and URI.
	 */
	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(
			@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}
}

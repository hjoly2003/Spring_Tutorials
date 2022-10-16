package com.example.serviceregistrationanddiscoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * An Eureka Service registry with which other applications can communicate.<p/> 
 * [N]:eureka-server - {@code @EnableEurekaServer} To enable the service registry.
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistrationAndDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationAndDiscoveryServiceApplication.class, args);
	}
}

package com.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/** [N]:oauth2 - the {@code @EnableAuthorizationServer} annotation to enable the support for the authorization server.
 * 
*/
@EnableAuthorizationServer
@SpringBootApplication
public class OpenapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenapiApplication.class, args);
	}
}

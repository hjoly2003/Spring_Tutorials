package com.proto.greet;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

/**
 * curl http://localhost:8081/hello
 */
@SpringBootApplication
@RestController
@RequestMapping(value="hello")
public class GreetingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}

	@GetMapping
	public ResponseEntity<String> greet() {
		return ResponseEntity.ok("Allo le monde!");
	}
}

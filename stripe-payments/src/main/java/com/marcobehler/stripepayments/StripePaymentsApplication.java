package com.marcobehler.stripepayments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class StripePaymentsApplication {

	@Value("${stripe.api.key}")
	private String stripeApiKey;

  // [N]:stripeApiKey
  @PostConstruct
	public void init() {
		Stripe.apiKey = stripeApiKey;
	}

	public static void main(String[] args) {
		SpringApplication.run(StripePaymentsApplication.class, args);
	}

}

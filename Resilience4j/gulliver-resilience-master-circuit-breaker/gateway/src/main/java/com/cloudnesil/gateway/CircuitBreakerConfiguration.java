package com.cloudnesil.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * [N]:resilience]:circuit - Circuit breaker configuration
 */
@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(CircuitBreakerConfig.custom()

                // Specifies sliding window size as 20 and failure rate threshold as 50. This means that out of the last 20 requests, if 50% percent of them fail the circuit breaker goes to OPEN state.
                .slidingWindowSize(20)
                .failureRateThreshold(50)

                // After 5 requests the circuit breaker goes into OPEN or CLOSED state depending on the failure rate.
                .permittedNumberOfCallsInHalfOpenState(5)

                // Controls how long the CircuitBreaker should stay OPEN before it switches to HALF_OPEN is set to 60 seconds.
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .build())
            .build()
        );
    }
}

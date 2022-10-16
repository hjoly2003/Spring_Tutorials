/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.api.boundary.web;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.samples.petclinic.api.application.CustomersServiceClient;
import org.springframework.samples.petclinic.api.application.VisitsServiceClient;
import org.springframework.samples.petclinic.api.dto.OwnerDetails;
import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Maciej Szarlinski
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gateway")
public class ApiGatewayController {

    private final CustomersServiceClient customersServiceClient;

    private final VisitsServiceClient visitsServiceClient;

    // [N]:resilience]:circuit - When you include a Spring Cloud Circuit Breaker starter on your classpath, a bean that implements this API is automatically created and added to the {@code ApiGatewayController} constructor as an argument.
    private final ReactiveCircuitBreakerFactory cbFactory;

    @GetMapping(value = "owners/{ownerId}")
    public Mono<OwnerDetails> getOwnerDetails(final @PathVariable int ownerId) {
        return customersServiceClient.getOwner(ownerId) // [N]:eureka-client
            .flatMap(owner ->
                visitsServiceClient.getVisitsForPets(owner.getPetIds()) // [N]:eureka-client
                    .transform(it -> {
                        // [N]:resilience]:circuit - To create a reactive cicuit breaker (See https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#circuit-breakers-in-reactive-code)
                        ReactiveCircuitBreaker cb = cbFactory.create("getOwnerDetails");
                        // [N]:resilience]:circuit - The run method takes a) your code to be wrapped in the circuit breaker and b) a fallback that is run if the circuit breaker is tripped.
                        return cb.run(it, throwable -> emptyVisitsForPets());
                    })
                    .map(addVisitsToOwner(owner))
            );

    }

    /**
     * The fallback that is run if the circuit breaker is tripped.
     * @param owner
     * @return
     */
    private Function<Visits, OwnerDetails> addVisitsToOwner(OwnerDetails owner) {
        return visits -> {
            owner.getPets()
                .forEach(pet -> pet.getVisits()
                    .addAll(visits.getItems().stream()
                        .filter(v -> v.getPetId() == pet.getId())
                        .collect(Collectors.toList()))
                );
            return owner;
        };
    }

    private Mono<Visits> emptyVisitsForPets() {
        return Mono.just(new Visits());
    }
}

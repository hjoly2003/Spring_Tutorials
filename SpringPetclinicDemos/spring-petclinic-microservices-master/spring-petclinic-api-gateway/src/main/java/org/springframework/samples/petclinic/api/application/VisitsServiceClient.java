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
package org.springframework.samples.petclinic.api.application;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * [N]:eureka-client:load-balance - Uses a Load Balancer–aware {@code WebClient} to access a {@code Visits} from a {@code VisitsService} client.<p/> Rather than using the physical location of the service in the RestTemplate call, you need to build the target URL using the Eureka service ID of the service you want to call. 
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/3.1.1/reference/html/#webclinet-loadbalancer-client">Spring WebClient as a Load Balancer Client</a>
 * @author Maciej Szarlinski
 */
@Component
@RequiredArgsConstructor
public class VisitsServiceClient {

    // Could be changed for testing purpose
    private String hostname = "http://visits-service/";

    private final WebClient.Builder webClientBuilder;

    public Mono<Visits> getVisitsForPets(final List<Integer> petIds) {

        // [N]:eureka-client] - Uses a target URL with the Eureka service ID. The server name in the URL matches the application ID of the Visits service that you used to register with Eureka  (See spring.application.name in the application.yml of the Visits Service).
        return webClientBuilder.build()
            .get()
            .uri(hostname + "pets/visits?petId={petId}", joinIds(petIds))
            .retrieve()
            .bodyToMono(Visits.class);
    }

    private String joinIds(List<Integer> petIds) {
        return petIds.stream().map(Object::toString).collect(joining(","));
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }
}

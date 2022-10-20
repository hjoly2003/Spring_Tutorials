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
import org.springframework.samples.petclinic.api.dto.OwnerDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * [N]:eureka-client:load-balance - Uses a Load Balancer–aware {@code WebClient} to access a {@code OwnerDetails} from a {@code CustomersService} client. 
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/3.1.1/reference/html/#webclinet-loadbalancer-client">Spring WebClient as a Load Balancer Client</a>
 * @author Maciej Szarlinski
 */
@Component
@RequiredArgsConstructor
public class CustomersServiceClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<OwnerDetails> getOwner(final int ownerId) {

        // [N]:eureka-client] - Uses a target URL with the Eureka service ID. The server name in the URL matches the application ID of the Customers service that you used to register with Eureka  (See spring.application.name in the application.yml of the Customers Service).
        return webClientBuilder.build().get()
            .uri("http://customers-service/owners/{ownerId}", ownerId)
            .retrieve()
            .bodyToMono(OwnerDetails.class);
    }
}

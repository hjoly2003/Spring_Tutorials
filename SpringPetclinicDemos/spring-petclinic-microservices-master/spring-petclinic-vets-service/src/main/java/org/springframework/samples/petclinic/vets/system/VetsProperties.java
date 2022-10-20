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
package org.springframework.samples.petclinic.vets.system;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * [N]:config]:client - Typesafe custom configuration.<p/>
 * [N]:config]:client - The {@code @ConfigurationProperties} annotation injects all the {@code vets}-prefixed properties from the <em>Spring Cloud Configuration Server</em> and injects these into the name-matching attributes of the annoted class. Specifically, it'll fetch {@code vets.cache} from <a href="https://github.com/spring-petclinic/spring-petclinic-microservices-config/blob/main/vets-service.yml">vets-service.yml</a> and map it to the {@code VetsProperties#cache}.<p/>
 * See <a href="https://www.baeldung.com/configuration-properties-in-spring-boot">Guide to {@code @ConfigurationProperties}  in Spring Boot | Baeldung</a>
 *
 * @author Maciej Szarlinski
 */
@Data
@ConfigurationProperties(prefix = "vets")
public class VetsProperties {

    private Cache cache;

    @Data
    public static class Cache {

        private int ttl;

        private int heapSize;
    }
}

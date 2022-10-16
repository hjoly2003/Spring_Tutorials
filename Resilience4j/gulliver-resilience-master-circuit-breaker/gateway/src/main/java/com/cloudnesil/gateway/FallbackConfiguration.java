package com.cloudnesil.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * [N]:resilience]:circuit - A configuration class to return an empty Mono as a response when the Restaurant microservice is down.
 */
@Configuration
public class FallbackConfiguration {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions.route(RequestPredicates.GET("restaurant-fallback"), this::handleGetFallback);
    }
    public Mono<ServerResponse> handleGetFallback(ServerRequest serverRequest){
        return ServerResponse.ok().body(Mono.empty(), String.class);
    }
}

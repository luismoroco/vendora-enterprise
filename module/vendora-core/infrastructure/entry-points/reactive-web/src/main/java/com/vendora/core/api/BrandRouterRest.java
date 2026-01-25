package com.vendora.core.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BrandRouterRest {

    private final BrandHandler handler;

    @Bean
    public RouterFunction<ServerResponse> brandRouter() {
        return route(POST("/api/v1/brands"), this.handler::createBrand)
            .andRoute(PUT("/api/v1/brands"), this.handler::updateBrand);
    }
}

package com.vendora.core.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ProductRouterRest {

    private final ProductHandler handler;

    @Bean
    public RouterFunction<ServerResponse> productRouter() {
        return route(POST("/api/v1/products"), this.handler::createProduct)
            .andRoute(PUT("/api/v1/products/{product-id}"), this.handler::updateProduct)
            .andRoute(GET("/api/v1/products/{product-id}"), this.handler::getProduct)
            .andRoute(GET("/api/v1/products"), this.handler::getProducts);
    }
}


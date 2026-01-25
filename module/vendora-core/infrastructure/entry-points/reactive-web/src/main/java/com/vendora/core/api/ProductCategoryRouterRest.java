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
public class ProductCategoryRouterRest {

    private final ProductCategoryHandler handler;

    @Bean
    public RouterFunction<ServerResponse> productCategoryRouter() {
        return route(POST("/api/v1/product-categories"), this.handler::createProductCategory)
            .andRoute(PUT("/api/v1/product-categories/{product-category-id}"), this.handler::updateProductCategory);
    }
}


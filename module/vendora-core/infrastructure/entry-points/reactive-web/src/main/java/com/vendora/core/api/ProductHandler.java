package com.vendora.core.api;

import com.vendora.core.usecase.ProductUseCase;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.GetProductDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private static final String PRODUCT_ID = "productId";

    private final ProductUseCase useCase;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateProductDTO.class)
            .flatMap(this.useCase::createProduct)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateProductDTO.class)
            .flatMap(this.useCase::updateProduct)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }

    public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(GetProductDTO.class)
            .map(request -> {
                request.setProductId(Long.valueOf(serverRequest.pathVariable(PRODUCT_ID)));
                return request;
            })
            .flatMap(this.useCase::getProduct)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }
}


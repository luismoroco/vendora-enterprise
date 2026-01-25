package com.vendora.core.api;

import com.vendora.core.usecase.ProductCategoryUseCase;
import com.vendora.core.usecase.dto.CreateProductCategoryDTO;
import com.vendora.core.usecase.dto.UpdateProductCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductCategoryHandler {

    private final ProductCategoryUseCase useCase;

    public Mono<ServerResponse> createProductCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateProductCategoryDTO.class)
            .flatMap(this.useCase::createProductCategory)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }

    public Mono<ServerResponse> updateProductCategory(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateProductCategoryDTO.class)
            .flatMap(this.useCase::updateProductCategory)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }
}


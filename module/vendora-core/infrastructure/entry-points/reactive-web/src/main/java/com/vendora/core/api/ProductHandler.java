package com.vendora.core.api;

import com.vendora.common.Paginator;
import com.vendora.core.model.Product;
import com.vendora.core.usecase.ProductUseCase;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.GetProductDTO;
import com.vendora.core.usecase.dto.GetProductsDTO;
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

    private static final String PRODUCT_ID = "product_id";
    private static final String NAME_PARAM = "name";
    private static final String PRODUCT_STATUS_TYPES_PARAM = "product_status_types";
    private static final String BAR_CODE_PARAM = "bar_code";
    private static final String PROVIDER_IDS_PARAM = "provider_ids";
    private static final String BRAND_IDS_PARAM = "brand_ids";
    private static final String CATEGORY_IDS_PARAM = "category_ids";

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
            .map(request -> {
                request.setProductId(Long.valueOf(serverRequest.pathVariable(PRODUCT_ID)));
                return request;
            })
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

    public Mono<ServerResponse> getProducts(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(GetProductsDTO.class)
            .map(request -> {
                request.setPage(serverRequest.queryParam(Paginator.PAGE_PARAM)
                    .map(Integer::valueOf)
                    .orElse(Paginator.DEFAULT_PAGE));

                request.setPageSize(serverRequest.queryParam(Paginator.PAGE_SIZE_PARAM)
                    .map(Integer::valueOf)
                    .orElse(Paginator.DEFAULT_PAGE_SIZE));

                return request;
            })
            .flatMap(request ->
                Mono.zip(
                    this.useCase.getProducts(request).collectList(),
                    this.useCase.countProducts(request)
                )
                .map(tuple ->
                    Paginator.<Product>builder()
                        .content(tuple.getT1())
                        .total(tuple.getT2())
                        .page(request.getPage())
                        .size(request.getPageSize())
                        .build()
            )
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            ));
    }
}


package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Mono<Void> verifyProductNameUniquenessWithinTenantOrThrow(String productName, Long tenantId) {
        return this.repository.existsByProductNameAndTenantId(productName, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Void> verifyBarCodeUniquenessWithinTenantOrThrow(String barCode, Long tenantId) {
        return this.repository.existsByBarCodeAndTenantId(barCode, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Product> findByProductIdAndTenantIdOrThrow(Long productId, Long tenantId) {
        return this.repository.findByProductIdAndTenantId(productId, tenantId)
          .switchIfEmpty(Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(Product.TYPE))));
    }
 }

package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.ProductCategory;
import com.vendora.core.model.gateway.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public Mono<Void> verifyProductCategoryNameUniquenessWithinTenantOrThrow(String name, Long tenantId) {
        return this.repository.existsByNameAndTenantId(name, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(ProductCategory.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<ProductCategory> findByProductCategoryIdAndTenantIdOrThrow(Long productCategoryId, Long tenantId) {
        return this.repository.findByProductCategoryIdAndTenantId(productCategoryId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(ProductCategory.TYPE))));
    }

    public Mono<Void> existsByProductCategoryIdAndTenantIdOrThrow(Long productCategoryId, Long tenantId) {
        return this.repository.existsByProductCategoryIdAndTenantId(productCategoryId, tenantId)
            .flatMap(flag -> !flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(ProductCategory.TYPE)))
                : Mono.empty()
            );
    }
}


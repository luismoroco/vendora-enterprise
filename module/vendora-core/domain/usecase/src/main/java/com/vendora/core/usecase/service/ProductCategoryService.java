package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.ProductCategory;
import com.vendora.core.model.gateway.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.ProductCategory.NAME;

@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public Mono<ProductCategory> getByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId) {
        return this.repository.findByProductCategoryIdAndTenantId(productCategoryId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(ProductCategory.TYPE))));
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyNameConstraints(String name, Long tenantId) {
        return ValidatorUtils.string(NAME, name)
            .then(this.repository.existsByNameAndTenantId(name, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(ProductCategory.TYPE)))
                    : Mono.empty()
                )
            );
    }
}


package com.vendora.core.model.gateway;

import com.vendora.core.model.ProductCategory;
import reactor.core.publisher.Mono;

public interface ProductCategoryRepository {

    Mono<ProductCategory> save(ProductCategory productCategory);

    Mono<ProductCategory> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<ProductCategory> findByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId);

    Mono<Boolean> existsByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId);

    Mono<ProductCategory> findByProductCategoryId(Long productCategoryId);
}

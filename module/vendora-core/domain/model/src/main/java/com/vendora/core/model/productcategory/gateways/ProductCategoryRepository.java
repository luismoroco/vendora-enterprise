package com.vendora.core.model.productcategory.gateways;

import com.vendora.core.model.productcategory.ProductCategory;
import reactor.core.publisher.Mono;

public interface ProductCategoryRepository {

    Mono<ProductCategory> save(ProductCategory productCategory);

    Mono<ProductCategory> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);
}

package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.ProductCategoryEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductCategoryReactiveRepository extends ReactiveCrudRepository<ProductCategoryEntity, Long>, ReactiveQueryByExampleExecutor<ProductCategoryEntity> {

    Mono<ProductCategoryEntity> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<ProductCategoryEntity> findByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId);

    Mono<Boolean> existsByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId);
}


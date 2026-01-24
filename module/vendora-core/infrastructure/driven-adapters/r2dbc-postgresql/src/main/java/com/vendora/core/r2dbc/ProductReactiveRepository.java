package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, Long>, ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<ProductEntity> findByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId);
}


package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.BrandEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BrandReactiveRepository extends ReactiveCrudRepository<BrandEntity, Long>, ReactiveQueryByExampleExecutor<BrandEntity> {

    Mono<BrandEntity> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);
}

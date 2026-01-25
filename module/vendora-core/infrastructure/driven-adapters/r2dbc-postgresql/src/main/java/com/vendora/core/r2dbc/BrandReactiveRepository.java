package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.BrandEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BrandReactiveRepository extends ReactiveCrudRepository<BrandEntity, Long>, ReactiveQueryByExampleExecutor<BrandEntity> {

    Mono<BrandEntity> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<BrandEntity> findByBrandIdAndTenantId(Long brandId, Long tenantId);

    Mono<Boolean> existsByBrandIdAndTenantId(Long brandId, Long tenantId);

    Mono<Integer> countByBrandIdInAndTenantId(List<Long> ids, Long tenantId);
}

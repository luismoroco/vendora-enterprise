package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.ProviderEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProviderReactiveRepository extends ReactiveCrudRepository<ProviderEntity, Long>, ReactiveQueryByExampleExecutor<ProviderEntity> {

    Mono<ProviderEntity> findByRucAndTenantId(String ruc, Long tenantId);

    Mono<Boolean> existsByRucAndTenantId(String ruc, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<ProviderEntity> findByProviderIdAndTenantId(Long providerId, Long tenantId);

    Mono<Boolean> existsByProviderIdAndTenantId(Long providerId, Long tenantId);
}


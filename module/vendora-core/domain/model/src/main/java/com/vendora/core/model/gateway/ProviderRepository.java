package com.vendora.core.model.gateway;

import com.vendora.core.model.Provider;
import reactor.core.publisher.Mono;

public interface ProviderRepository {

    Mono<Provider> save(Provider provider);

    Mono<Provider> findByRucAndTenantId(String ruc, Long tenantId);

    Mono<Boolean> existsByRucAndTenantId(String ruc, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<Provider> findByProviderIdAndTenantId(Long providerId, Long tenantId);

    Mono<Boolean> existsByProviderIdAndTenantId(Long providerId, Long tenantId);
}

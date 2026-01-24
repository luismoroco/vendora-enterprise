package com.vendora.core.model.provider.gateways;

import com.vendora.core.model.provider.Provider;
import reactor.core.publisher.Mono;

public interface ProviderRepository {

    Mono<Provider> save(Provider provider);

    Mono<Provider> findByRucAndTenantId(String ruc, Long tenantId);

    Mono<Boolean> existsByRucAndTenantId(String ruc, Long tenantId);
}

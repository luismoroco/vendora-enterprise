package com.vendora.core.model.gateway;

import com.vendora.core.model.Tenant;
import reactor.core.publisher.Mono;

public interface TenantRepository {

    Mono<Boolean> existsByName(String name);

    Mono<Tenant> save(Tenant tenant);
}

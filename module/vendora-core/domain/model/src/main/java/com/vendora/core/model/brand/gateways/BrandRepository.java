package com.vendora.core.model.brand.gateways;

import com.vendora.core.model.brand.Brand;
import reactor.core.publisher.Mono;

public interface BrandRepository {

    Mono<Brand> save(Brand brand);

    Mono<Brand> findByNameAndTenantId(String name, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);
}

package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.common.exc.NotFoundException;
import com.vendora.core.model.Brand;
import com.vendora.core.model.gateway.BrandRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository repository;

    public Mono<Brand> findByBrandIdAndTenantIdOrThrow(Long brandId, Long tenantId) {
        return this.repository.findByBrandIdAndTenantId(brandId, tenantId)
            .switchIfEmpty(Mono.error(new NotFoundException(LogCatalog.ENTITY_NOT_FOUND.of(Brand.TYPE))));
    }

    public Mono<Void> verifyNameUniquenessOrThrow(String name, Long tenantId) {
        return this.repository.existsByNameAndTenantId(name, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Brand.TYPE)))
                : Mono.empty()
            );
    }
}

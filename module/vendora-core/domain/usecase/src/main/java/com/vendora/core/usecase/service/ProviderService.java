package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Provider;
import com.vendora.core.model.gateway.ProviderRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository repository;

    public Mono<Void> verifyProviderNameUniquenessWithinTenantOrThrow(String name, Long tenantId) {
        return this.repository.existsByNameAndTenantId(name, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Provider.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Provider> findByProviderIdAndTenantIdOrThrow(Long providerId, Long tenantId) {
        return this.repository.findByProviderIdAndTenantId(providerId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(Provider.TYPE))));
    }

    public Mono<Void> existsByProviderIdAndTenantIdOrThrow(Long providerId, Long tenantId) {
        return this.repository.existsByProviderIdAndTenantId(providerId, tenantId)
            .flatMap(flag -> !flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(Provider.TYPE)))
                : Mono.empty()
            );
    }
}

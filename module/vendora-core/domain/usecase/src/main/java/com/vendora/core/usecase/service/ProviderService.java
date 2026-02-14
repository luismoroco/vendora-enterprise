package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Provider;
import com.vendora.core.model.gateway.ProviderRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.Provider.NAME;

@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository repository;

    public Mono<Provider> getByProviderIdAndTenantId(Long providerId, Long tenantId) {
        return this.repository.findByProviderIdAndTenantId(providerId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(Provider.TYPE))));
    }

    public Mono<Void> verifyProviderExists(Long providerId, Long tenantId) {
        return this.repository.existsByProviderIdAndTenantId(providerId, tenantId)
            .flatMap(flag -> !flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(Provider.TYPE)))
                : Mono.empty()
            );
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyNameConstraints(String name, Long tenantId) {
        return ValidatorUtils.string(NAME, name)
            .then(this.repository.existsByNameAndTenantId(name, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Provider.TYPE)))
                    : Mono.empty()
                )
            );
    }
}

package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import com.vendora.core.usecase.dto.CreateTenantDTO;
import com.vendora.core.usecase.dto.UpdateTenantDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.Tenant.DOMAIN;
import static com.vendora.core.model.Tenant.NAME;

@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repository;

    public Mono<Tenant> getByTenantId(Long tenantId) {
        return this.repository.findById(tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(Tenant.TYPE))));
    }

    /**
     * DTO-level Validator
     * */

    public Mono<Void> validateDTO(CreateTenantDTO dto) {
        return Mono.when(
            this.verifyNameConstraints(dto.getName()),
            this.verifyDomainConstraints(dto.getDomain())
        );
    }

    public Mono<Void> validateDTO(UpdateTenantDTO dto) {
        return Mono.when(
            Mono.justOrEmpty(dto.getName())
                .flatMap(this::verifyNameConstraints),
            Mono.justOrEmpty(dto.getDomain())
                .flatMap(this::verifyDomainConstraints)
        );
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyNameConstraints(String name) {
        return ValidatorUtils.string(NAME, name)
            .then(this.repository.existsByName(name)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Tenant.TYPE)))
                    : Mono.empty()
                )
            );
    }

    public Mono<Void> verifyDomainConstraints(String domain) {
        return ValidatorUtils.string(DOMAIN, domain)
            .then(this.repository.existsByDomain(domain)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Tenant.TYPE)))
                    : Mono.empty()
                )
            );
    }
}

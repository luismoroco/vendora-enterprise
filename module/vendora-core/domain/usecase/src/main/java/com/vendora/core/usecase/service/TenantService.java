package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repository;

    public Mono<Tenant> getByTenantId(Long tenantId) {
        return this.repository.findById(tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(Tenant.TYPE))));
    }

    /**
     * Validation
     * */

    public Mono<Void> verifyNameConstraints(String name) {
        return this.repository.existsByName(name)
          .flatMap(flag -> flag.equals(Boolean.TRUE)
            ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Tenant.TYPE)))
            : Mono.empty()
          );
    }

    public Mono<Void> verifyDomainConstraints(String domain) {
        return this.repository.existsByDomain(domain)
          .flatMap(flag -> flag.equals(Boolean.TRUE)
            ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Tenant.TYPE)))
            : Mono.empty()
          );
    }
}

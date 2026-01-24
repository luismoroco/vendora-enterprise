package com.vendora.core.usecase;

import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import com.vendora.core.usecase.dto.CreateTenantDTO;
import com.vendora.core.usecase.dto.UpdateTenantDTO;
import com.vendora.core.usecase.service.TenantService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TenantUseCase {

    private final TenantRepository repository;
    private final TenantService service;

    public Mono<Tenant> createTenant(CreateTenantDTO dto) {
        return this.service.verifyTenantNameUniquenessOrThrow(dto.getName())
            .then(this.repository.save(
                Tenant.builder()
                    .name(dto.getName())
                    .domain(dto.getDomain())
                    .build()
          ));
    }

    public Mono<Tenant> updateTenant(UpdateTenantDTO dto) {
        return this.service.findByTenantIdOrThrow(dto.getTenantId())
            .flatMap(tenant ->
                Mono.justOrEmpty(dto.getName())
                    .filter(name -> !name.equals(tenant.getName()))
                    .flatMap(name ->
                        this.service.verifyTenantNameUniquenessOrThrow(name)
                            .doOnSuccess(__ -> tenant.setName(name))
                            .thenReturn(tenant)
                    )
                    .defaultIfEmpty(tenant)
            )
            .flatMap(tenant ->
                Mono.justOrEmpty(dto.getDomain())
                    .filter(domain -> !domain.equals(tenant.getDomain()))
                    .flatMap(domain ->
                        this.service.verifyDomainUniquenessOrThrow(domain)
                            .thenReturn(domain)
                    )
                    .doOnNext(tenant::setDomain)
                    .thenReturn(tenant)
                    .defaultIfEmpty(tenant)
            )
            .flatMap(this.repository::save);
    }
}

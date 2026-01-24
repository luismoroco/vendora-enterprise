package com.vendora.core.usecase.tenant;

import com.vendora.core.model.tenant.Tenant;
import com.vendora.core.model.tenant.gateways.TenantRepository;
import com.vendora.core.usecase.tenant.dto.CreateTenantDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TenantUseCase {

    private final TenantRepository repository;

    Mono<Tenant> create(CreateTenantDTO dto) {
        return Mono.just(new Tenant());
    }
}

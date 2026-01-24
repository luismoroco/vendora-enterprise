package com.vendora.core.usecase;

import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import com.vendora.core.usecase.dto.CreateTenantDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TenantUseCase {

    private final TenantRepository repository;

    Mono<Tenant> create(CreateTenantDTO dto) {
        return Mono.just(new Tenant());
    }
}

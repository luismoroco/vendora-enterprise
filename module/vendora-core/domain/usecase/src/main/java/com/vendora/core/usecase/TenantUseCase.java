package com.vendora.core.usecase;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import com.vendora.core.usecase.dto.CreateTenantDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TenantUseCase {

    private final TenantRepository repository;

    Mono<Tenant> create(CreateTenantDTO dto) {
        return this.repository.existsByName(dto.getName())
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Tenant.TYPE)))
                : Mono.empty()
            )
            .then(Mono.defer(() -> {
                Tenant tenant = Tenant.builder()
                    .name(dto.getName())
                    .domain(dto.getDomain())
                    .build();

                return this.repository.save(tenant);
            }));
    }
}

package com.vendora.core.usecase.brand;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.brand.Brand;
import com.vendora.core.model.brand.gateways.BrandRepository;
import com.vendora.core.usecase.brand.dto.CreateBrandDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BrandUseCase {

    private final BrandRepository repository;

    public Mono<Brand> createBrand(CreateBrandDTO dto) {
        return this.repository.existsByNameAndTenantId(dto.getName(), dto.getTenantId())
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Brand.TYPE)))
                : Mono.empty()
            )
            .then(Mono.defer(() -> {
                Brand brand = Brand.builder()
                    .name(dto.getName())
                    .imageUrl(dto.getImageUrl())
                    .tenantId(dto.getTenantId())
                    .build();

                return this.repository.save(brand);
            }));
    }
}

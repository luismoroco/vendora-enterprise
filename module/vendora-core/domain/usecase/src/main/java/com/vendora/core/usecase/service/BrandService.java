package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.common.exc.NotFoundException;
import com.vendora.core.model.Brand;
import com.vendora.core.model.gateway.BrandRepository;
import com.vendora.core.usecase.dto.CreateBrandDTO;
import com.vendora.core.usecase.dto.UpdateBrandDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.Brand.IMAGE_URL;
import static com.vendora.core.model.Brand.NAME;

@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository repository;

    public Mono<Brand> getByBrandIdAndTenantId(Long brandId, Long tenantId) {
        return this.repository.findByBrandIdAndTenantId(brandId, tenantId)
            .switchIfEmpty(Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(Brand.TYPE))));
    }

    public Mono<Void> verifyBrandExists(Long brandId, Long tenantId) {
        return this.repository.existsByBrandIdAndTenantId(brandId, tenantId)
            .flatMap(flag -> flag.equals(Boolean.FALSE)
                ? Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(Brand.TYPE)))
                : Mono.empty()
            );
    }

    /**
     * DTO-level Validator
     * */

    public Mono<Void> validateDTO(CreateBrandDTO dto) {
        return Mono.when(
            verifyNameConstraints(dto.getName(), dto.getTenantId()),
            verifyImageUrlConstraints(dto.getImageUrl())
        );
    }

    public Mono<Void> validateDTO(UpdateBrandDTO dto) {
        return Mono.when(
            Mono.justOrEmpty(dto.getName())
                .flatMap(name -> verifyNameConstraints(name, dto.getTenantId())),
            Mono.justOrEmpty(dto.getImageUrl())
                .flatMap(BrandService::verifyImageUrlConstraints)
        );
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyNameConstraints(String name, Long tenantId) {
        return ValidatorUtils.string(NAME, name)
            .then(this.repository.existsByNameAndTenantId(name, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Brand.TYPE)))
                    : Mono.empty()
                )
            );
    }

    public static Mono<Void> verifyImageUrlConstraints(String imageUrl) {
        return ValidatorUtils.uri(IMAGE_URL, imageUrl);
    }
}

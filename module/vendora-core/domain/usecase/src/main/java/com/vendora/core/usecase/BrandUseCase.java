package com.vendora.core.usecase;

import com.vendora.core.model.Brand;
import com.vendora.core.model.gateway.BrandRepository;
import com.vendora.core.usecase.dto.CreateBrandDTO;
import com.vendora.core.usecase.dto.UpdateBrandDTO;
import com.vendora.core.usecase.service.BrandService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BrandUseCase {

    private final BrandRepository repository;
    private final BrandService service;

    public Mono<Brand> createBrand(CreateBrandDTO dto) {
        return this.service.requireUniqueBrandName(dto.getName(), dto.getTenantId())
            .then(this.repository.save(
                Brand.builder()
                    .name(dto.getName())
                    .imageUrl(dto.getImageUrl())
                    .tenantId(dto.getTenantId())
                    .build()
          ));
    }

    public Mono<Brand> updateBrand(UpdateBrandDTO dto) {
        return this.service.findByBrandIdAndTenantIdOrThrow(dto.getBrandId(), dto.getTenantId())
            .flatMap(brand ->
                Mono.justOrEmpty(dto.getName())
                    .filter(name -> !name.equals(brand.getName()))
                    .flatMap(name ->
                        this.service.requireUniqueBrandName(name, dto.getTenantId())
                            .doOnSuccess(__ -> brand.setName(name))
                            .thenReturn(brand)
                    )
                    .defaultIfEmpty(brand)
            )
            .flatMap(brand ->
                Mono.justOrEmpty(dto.getImageUrl())
                    .filter(imageUrl -> !imageUrl.equals(brand.getImageUrl()))
                    .doOnNext(brand::setImageUrl)
                    .thenReturn(brand)
                    .defaultIfEmpty(brand)
            )
            .flatMap(this.repository::save);
    }
}

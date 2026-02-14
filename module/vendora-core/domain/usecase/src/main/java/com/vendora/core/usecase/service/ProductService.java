package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProviderService providerService;
    private final BrandService brandService;

    public Mono<Product> getByProductIdAndTenantId(Long productId, Long tenantId) {
        return this.repository.findByProductIdAndTenantId(productId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(Product.TYPE))));
    }

    /**
     * DTO-level Validator
     * */

    public Mono<Void> validateDTO(CreateProductDTO dto) {
        return Mono.when(
            this.verifyNameConstraints(dto.getName(), dto.getTenantId()),
            this.verifyBarCodeConstraints(dto.getBarCode(), dto.getTenantId()),
            this.verifyProviderConstraints(dto.getProviderId(), dto.getTenantId()),
            this.verifyBrandConstraints(dto.getBrandId(), dto.getTenantId())
        );
    }

    public Mono<Void> validateDTO(UpdateProductDTO dto) {
        return Mono.when(
            Mono.justOrEmpty(dto.getName())
                .flatMap(name -> this.verifyNameConstraints(name, dto.getTenantId())),
            Mono.justOrEmpty(dto.getBarCode())
                .flatMap(barCode -> this.verifyBarCodeConstraints(barCode, dto.getTenantId())),
            Mono.justOrEmpty(dto.getProviderId())
                .flatMap(providerId -> this.verifyProviderConstraints(providerId, dto.getTenantId())),
            Mono.justOrEmpty(dto.getBrandId())
                .flatMap(brandId -> this.verifyBrandConstraints(brandId, dto.getTenantId()))
        );
    }

    /**
     * Validation
     * */

    public Mono<Void> verifyNameConstraints(String productName, Long tenantId) {
        return this.repository.existsByProductNameAndTenantId(productName, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Void> verifyBarCodeConstraints(String barCode, Long tenantId) {
        return this.repository.existsByBarCodeAndTenantId(barCode, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Void> verifyProviderConstraints(Long providerId, Long tenantId) {
        return this.providerService.verifyProviderExists(providerId, tenantId);
    }

    public Mono<Void> verifyBrandConstraints(Long brandId, Long tenantId) {
        return this.brandService.verifyBrandExists(brandId, tenantId);
    }
 }

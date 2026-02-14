package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

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
            this.verifyBrandConstraints(dto.getBrandId(), dto.getTenantId()),
            verifyPriceConstraints(dto.getPrice()),
            verifyStockConstraints(dto.getStock()),
            verifyCostConstraints(dto.getCost()),
            verifyImageUrlConstraints(dto.getImageUrl()),
            verifyDescriptionConstraints(dto.getDescription())
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
                .flatMap(brandId -> this.verifyBrandConstraints(brandId, dto.getTenantId())),
            Mono.justOrEmpty(dto.getPrice())
                .flatMap(ProductService::verifyPriceConstraints),
            Mono.justOrEmpty(dto.getStock())
                .flatMap(ProductService::verifyStockConstraints),
            Mono.justOrEmpty(dto.getCost())
                .flatMap(ProductService::verifyCostConstraints),
            Mono.justOrEmpty(dto.getImageUrl())
                .flatMap(ProductService::verifyImageUrlConstraints),
            Mono.justOrEmpty(dto.getDescription())
                .flatMap(ProductService::verifyDescriptionConstraints)
        );
    }

    /**
     * Validation - Instance methods (require repository access)
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

    /**
     * Validation - Static methods (no repository access needed)
     * */

    public static Mono<Void> verifyPriceConstraints(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("price")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyStockConstraints(Integer stock) {
        if (stock == null || stock < 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("stock")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyCostConstraints(BigDecimal cost) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("cost")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyImageUrlConstraints(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("imageUrl")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyDescriptionConstraints(String description) {
        if (description == null || description.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("description")));
        }
        return Mono.empty();
    }
}

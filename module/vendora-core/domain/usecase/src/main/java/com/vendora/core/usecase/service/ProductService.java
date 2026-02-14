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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

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
            this.verifyProviderConstraints(dto.getProviderId(), dto.getTenantId()),
            this.verifyNameConstraints(dto.getName(), dto.getTenantId()),
            this.verifyBarCodeConstraints(dto.getBarCode(), dto.getTenantId()),
            verifyPriceConstraints(dto.getPrice()),
            verifyStockConstraints(dto.getStock()),
            verifyImageUrlConstraints(dto.getImageUrl()),
            verifyCostConstraints(dto.getCost()),
            verifyDescriptionConstraints(dto.getDescription()),
            this.verifyBrandConstraints(dto.getBrandId(), dto.getTenantId())
        );
    }

    public Mono<Void> validateDTO(UpdateProductDTO dto) {
        return Mono.when(
            Mono.justOrEmpty(dto.getProviderId())
                .flatMap(providerId -> this.verifyProviderConstraints(providerId, dto.getTenantId())),
            Mono.justOrEmpty(dto.getName())
                .flatMap(name -> this.verifyNameConstraints(name, dto.getTenantId())),
            Mono.justOrEmpty(dto.getBarCode())
                .flatMap(barCode -> this.verifyBarCodeConstraints(barCode, dto.getTenantId())),
            Mono.justOrEmpty(dto.getPrice())
                .flatMap(ProductService::verifyPriceConstraints),
            Mono.justOrEmpty(dto.getStock())
                .flatMap(ProductService::verifyStockConstraints),
            Mono.justOrEmpty(dto.getImageUrl())
                .flatMap(ProductService::verifyImageUrlConstraints),
            Mono.justOrEmpty(dto.getCost())
                .flatMap(ProductService::verifyCostConstraints),
            Mono.justOrEmpty(dto.getDescription())
                .flatMap(ProductService::verifyDescriptionConstraints),
            Mono.justOrEmpty(dto.getBrandId())
                .flatMap(brandId -> this.verifyBrandConstraints(brandId, dto.getTenantId()))
        );
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyNameConstraints(String productName, Long tenantId) {
        if (Objects.isNull(productName) || productName.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("name")));
        }

        return this.repository.existsByProductNameAndTenantId(productName, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Void> verifyBarCodeConstraints(String barCode, Long tenantId) {
        if (Objects.isNull(barCode) || barCode.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("barCode")));
        }

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

    public static Mono<Void> verifyPriceConstraints(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("price")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyStockConstraints(Integer stock) {
        if (Objects.isNull(stock) || stock < 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("stock")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyCostConstraints(BigDecimal cost) {
        if (Objects.isNull(cost) || cost.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("cost")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyImageUrlConstraints(String imageUrl) {
        if (Objects.isNull(imageUrl) || imageUrl.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("imageUrl")));
        }
        try {
            new URI(imageUrl);
        } catch (URISyntaxException e) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("imageUrl")));
        }
        return Mono.empty();
    }

    public static Mono<Void> verifyDescriptionConstraints(String description) {
        if (Objects.isNull(description) || description.isBlank()) {
            return Mono.error(new BadRequestException(LogCatalog.INVALID_PARAMETER.of("description")));
        }
        return Mono.empty();
    }
}

package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.common.LogCatalog.INVALID_PARAMETER;
import static com.vendora.core.model.Product.BAR_CODE;
import static com.vendora.core.model.Product.COST;
import static com.vendora.core.model.Product.DESCRIPTION;
import static com.vendora.core.model.Product.IMAGE_URL;
import static com.vendora.core.model.Product.NAME;
import static com.vendora.core.model.Product.PRICE;
import static com.vendora.core.model.Product.STOCK;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProviderService providerService;
    private final BrandService brandService;

    public Mono<Product> getByProductIdAndTenantId(Long productId, Long tenantId) {
        return this.repository.findByProductIdAndTenantId(productId, tenantId)
            .switchIfEmpty(Mono.error(new BadRequestException(ENTITY_NOT_FOUND.of(Product.TYPE))));
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
        return ValidatorUtils.string(NAME, productName)
            .then(this.repository.existsByProductNameAndTenantId(productName, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                    : Mono.empty()
                )
            );
    }

    public Mono<Void> verifyBarCodeConstraints(String barCode, Long tenantId) {
        return ValidatorUtils.string(BAR_CODE, barCode)
            .then(this.repository.existsByBarCodeAndTenantId(barCode, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(Product.TYPE)))
                    : Mono.empty()
                )
            );
    }

    public Mono<Void> verifyProviderConstraints(Long providerId, Long tenantId) {
        return this.providerService.verifyProviderExists(providerId, tenantId);
    }

    public Mono<Void> verifyBrandConstraints(Long brandId, Long tenantId) {
        return this.brandService.verifyBrandExists(brandId, tenantId);
    }

    public static Mono<Void> verifyPriceConstraints(BigDecimal price) {
        return ValidatorUtils.nonNull(PRICE, price)
            .then(Mono.defer(() -> price.compareTo(BigDecimal.ZERO) <= 0
                ? Mono.error(new BadRequestException(INVALID_PARAMETER.of(PRICE)))
                : Mono.empty()
            ));
    }

    public static Mono<Void> verifyStockConstraints(Integer stock) {
        return ValidatorUtils.nonNull(STOCK, stock)
            .then(Mono.defer(() -> stock < 0
                ? Mono.error(new BadRequestException(INVALID_PARAMETER.of(STOCK)))
                : Mono.empty()
            ));
    }

    public static Mono<Void> verifyCostConstraints(BigDecimal cost) {
        return ValidatorUtils.nonNull(COST, cost)
            .then(Mono.defer(() -> cost.compareTo(BigDecimal.ZERO) < 0
                ? Mono.error(new BadRequestException(INVALID_PARAMETER.of(COST)))
                : Mono.empty()
            ));
    }

    public static Mono<Void> verifyImageUrlConstraints(String imageUrl) {
        return ValidatorUtils.uri(IMAGE_URL, imageUrl);
    }

    public static Mono<Void> verifyDescriptionConstraints(String description) {
        return ValidatorUtils.string(DESCRIPTION, description);
    }
}

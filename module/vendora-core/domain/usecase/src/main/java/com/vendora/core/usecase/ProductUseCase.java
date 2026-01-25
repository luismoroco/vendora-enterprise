package com.vendora.core.usecase;

import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.BrandRepository;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.model.gateway.ProviderRepository;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.GetProductDTO;
import com.vendora.core.usecase.dto.GetProductsDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import com.vendora.core.usecase.service.BrandService;
import com.vendora.core.usecase.service.ProductService;
import com.vendora.core.usecase.service.ProviderService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository repository;
    private final ProductService service;
    private final ProviderService providerService;
    private final BrandService brandService;
    private final ProviderRepository providerRepository;
    private final BrandRepository brandRepository;

    public Mono<Product> createProduct(CreateProductDTO dto) {
        return this.service.verifyProductNameUniquenessWithinTenantOrThrow(dto.getName(), dto.getTenantId())
            .then(this.service.verifyBarCodeUniquenessWithinTenantOrThrow(dto.getBarCode(), dto.getTenantId()))
            .then(this.providerService.existsByProviderIdAndTenantIdOrThrow(dto.getProviderId(), dto.getTenantId()))
            .then(this.brandService.existsByBrandIdAndTenantIdOrThrow(dto.getBrandId(), dto.getTenantId()))
            .then(this.repository.save(
                Product.builder()
                    .name(dto.getName())
                    .barCode(dto.getBarCode())
                    .tenantId(dto.getTenantId())
                    .description(dto.getDescription())
                    .imageUrl(dto.getImageUrl())
                    .productStatusType(dto.getProductStatusType())
                    .imageUrl(dto.getImageUrl())
                    .tenantId(dto.getTenantId())
                    .cost(dto.getCost())
                    .brandId(dto.getBrandId())
                    .providerId(dto.getProviderId())
                    .price(dto.getPrice())
                    .stock(dto.getStock())
                    .build()
          ));
    }

    public Mono<Product> updateProduct(UpdateProductDTO dto) {
        return this.service.findByProductIdAndTenantIdOrThrow(dto.getProductId(), dto.getTenantId())
            .flatMap(product ->
                Mono.justOrEmpty(dto.getName())
                    .filter(name -> !name.equals(product.getName()))
                    .flatMap(name ->
                        this.service.verifyProductNameUniquenessWithinTenantOrThrow(name, dto.getTenantId())
                            .doOnSuccess(__ -> product.setName(name))
                            .thenReturn(product)
                    )
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getBarCode())
                    .filter(barCode -> !barCode.equals(product.getBarCode()))
                    .flatMap(barCode ->
                        this.service.verifyBarCodeUniquenessWithinTenantOrThrow(barCode, dto.getTenantId())
                            .doOnSuccess(__ -> product.setBarCode(barCode))
                            .thenReturn(product)
                    )
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getProviderId())
                    .filter(providerId -> !providerId.equals(product.getProviderId()))
                    .flatMap(providerId ->
                        this.providerService.existsByProviderIdAndTenantIdOrThrow(providerId, dto.getTenantId())
                            .doOnSuccess(__ -> product.setProviderId(providerId))
                            .thenReturn(product)
                    )
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getBrandId())
                    .filter(brandId -> !brandId.equals(product.getBrandId()))
                    .flatMap(brandId ->
                        this.brandService.existsByBrandIdAndTenantIdOrThrow(brandId, dto.getTenantId())
                            .doOnSuccess(__ -> product.setBrandId(brandId))
                            .thenReturn(product)
                    )
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getPrice())
                    .filter(price -> !price.equals(product.getPrice()))
                    .doOnNext(product::setPrice)
                    .thenReturn(product)
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getStock())
                    .filter(stock -> !stock.equals(product.getStock()))
                    .doOnNext(product::setStock)
                    .thenReturn(product)
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getImageUrl())
                    .filter(imageUrl -> !imageUrl.equals(product.getImageUrl()))
                    .doOnNext(product::setImageUrl)
                    .thenReturn(product)
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getCost())
                    .filter(cost -> !cost.equals(product.getCost()))
                    .doOnNext(product::setCost)
                    .thenReturn(product)
                    .defaultIfEmpty(product)
            )
            .flatMap(product ->
                Mono.justOrEmpty(dto.getDescription())
                    .filter(description -> !description.equals(product.getDescription()))
                    .doOnNext(product::setDescription)
                    .thenReturn(product)
                    .defaultIfEmpty(product)
            )
            .flatMap(this.repository::save);
    }

    public Mono<Product> getProduct(GetProductDTO dto) {
        return this.service.findByProductIdAndTenantIdOrThrow(dto.getProductId(), dto.getTenantId())
            .flatMap(product ->
                Mono.zip(
                    this.providerRepository.findByProviderId(product.getProviderId()),
                    this.brandRepository.findByBrandId(product.getBrandId())
                )
                .map(tuple -> {
                    product.setProvider(tuple.getT1());
                    product.setBrand(tuple.getT2());

                    return product;
              })
          );
    }

    public Flux<Product> getProducts(GetProductsDTO dto) {
        return this.repository.findProducts(
            dto.getTenantId(),
            dto.getName(),
            dto.getBarCode(),
            dto.getProviderIds(),
            dto.getBrandIds(),
            dto.getCategoryIds(),
            dto.getProductStatusTypes(),
            dto.getPage(),
            dto.getPageSize()
        );
    }

    public Mono<Integer> countProducts(GetProductsDTO dto) {
        return this.repository.countProducts(
            dto.getTenantId(),
            dto.getName(),
            dto.getBarCode(),
            dto.getProviderIds(),
            dto.getBrandIds(),
            dto.getCategoryIds(),
            dto.getProductStatusTypes()
        );
    }
}

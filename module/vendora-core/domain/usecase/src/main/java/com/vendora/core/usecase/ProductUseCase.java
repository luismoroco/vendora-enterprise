package com.vendora.core.usecase;

import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.usecase.dto.CreateProductDTO;
import com.vendora.core.usecase.dto.UpdateProductDTO;
import com.vendora.core.usecase.service.BrandService;
import com.vendora.core.usecase.service.ProductService;
import com.vendora.core.usecase.service.ProviderService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository repository;
    private final ProductService service;
    private final ProviderService providerService;
    private final BrandService brandService;

    public Mono<Product> createProduct(CreateProductDTO dto) {
        return this.service.verifyProductNameUniquenessWithinTenantOrThrow(dto.getName(), dto.getTenantId())
          .then(this.service.verifyBarCodeUniquenessWithinTenantOrThrow(dto.getBarCode(), dto.getTenantId()))
          .then(this.providerService.existsByProviderIdAndTenantIdOrThrow(dto.getProviderId(), dto.getTenantId()))
          .then(this.brandService.existsByBrandIdAndTenantIdOrThrow(dto.getBrandId(), dto.getTenantId()))
          .then(Mono.defer(() -> {
              Product product = new Product();
              product.setName(dto.getName());
              product.setProviderId(dto.getProviderId());
              product.setDescription(dto.getDescription());
              product.setBarCode(dto.getBarCode());
              product.setPrice(dto.getPrice());
              product.setStock(dto.getStock());
              product.setProductStatusType(dto.getProductStatusType());
              product.setImageUrl(dto.getImageUrl());
              product.setTenantId(dto.getTenantId());
              product.setCost(dto.getCost());
              product.setBrandId(dto.getBrandId());

              return Mono.just(product);
          }))
          .flatMap(this.repository::save);
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
}

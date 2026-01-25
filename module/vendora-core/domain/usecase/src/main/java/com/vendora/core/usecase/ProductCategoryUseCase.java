package com.vendora.core.usecase;

import com.vendora.core.model.ProductCategory;
import com.vendora.core.model.gateway.ProductCategoryRepository;
import com.vendora.core.usecase.dto.CreateProductCategoryDTO;
import com.vendora.core.usecase.dto.UpdateProductCategoryDTO;
import com.vendora.core.usecase.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductCategoryUseCase {

    private final ProductCategoryRepository repository;
    private final ProductCategoryService service;

    public Mono<ProductCategory> createProductCategory(CreateProductCategoryDTO dto) {
        return this.service.verifyProductCategoryNameUniquenessWithinTenantOrThrow(dto.getName(), dto.getTenantId())
            .then(this.repository.save(
                ProductCategory.builder()
                    .name(dto.getName())
                    .featured(dto.getFeatured())
                    .imageUrl(dto.getImageUrl())
                    .tenantId(dto.getTenantId())
                    .build()
            ));
    }

    public Mono<ProductCategory> updateProductCategory(UpdateProductCategoryDTO dto) {
        return this.service.findByProductCategoryIdAndTenantIdOrThrow(dto.getProductCategoryId(), dto.getTenantId())
            .flatMap(productCategory ->
                Mono.justOrEmpty(dto.getName())
                    .filter(name -> !name.equals(productCategory.getName()))
                    .flatMap(name ->
                        this.service.verifyProductCategoryNameUniquenessWithinTenantOrThrow(name, dto.getTenantId())
                            .doOnSuccess(__ -> productCategory.setName(name))
                            .thenReturn(productCategory)
                    )
                    .defaultIfEmpty(productCategory)
            )
            .flatMap(productCategory ->
                Mono.justOrEmpty(dto.getFeatured())
                    .filter(featured -> !featured.equals(productCategory.getFeatured()))
                    .doOnNext(productCategory::setFeatured)
                    .thenReturn(productCategory)
                    .defaultIfEmpty(productCategory)
            )
            .flatMap(productCategory ->
                Mono.justOrEmpty(dto.getImageUrl())
                    .filter(imageUrl -> !imageUrl.equals(productCategory.getImageUrl()))
                    .doOnNext(productCategory::setImageUrl)
                    .thenReturn(productCategory)
                    .defaultIfEmpty(productCategory)
            )
            .flatMap(this.repository::save);
    }
}

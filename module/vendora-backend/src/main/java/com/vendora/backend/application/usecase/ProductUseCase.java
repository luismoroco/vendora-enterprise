package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.*;
import com.vendora.backend.application.repository.ProductCategoryRepository;
import com.vendora.backend.application.repository.ProductRepository;
import com.vendora.backend.application.service.BrandService;
import com.vendora.backend.application.service.ProductService;
import com.vendora.backend.application.service.ProviderService;
import com.vendora.backend.application.usecase.request.CreateProductRequest;
import com.vendora.backend.application.usecase.request.GetProductsRequest;
import com.vendora.backend.application.usecase.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductUseCase {
  private final ProductRepository repository;
  private final ProductCategoryRepository productCategoryRepository;
  private final ProductService service;
  private final ProviderService providerService;
  private final BrandService brandService;

  public Product createProduct(CreateProductRequest request) {
    Provider provider = this.providerService.findByIdOrThrow(request.getProviderId());
    Brand brand = this.brandService.findByIdOrThrow(request.getBrandId());
    List<ProductCategory> categories = this.productCategoryRepository.findAllByProductCategoryIdIn(request.getProductCategoryIds());

    this.service.verifyBarCodeAvailability(request.getBarCode());
    this.service.verifyNameAvailability(request.getName());

    return this.repository.save(
      Product.builder()
        .productStatusType(ProductStatusType.ENABLED)
        .name(request.getName())
        .barCode(request.getBarCode())
        .price(request.getPrice())
        .stock(request.getStock())
        .provider(provider)
        .imageUrl(request.getImageUrl())
        .brand(brand)
        .categories(new HashSet<>(categories))
        .build()
    );
  }

  public Product updateProduct(UpdateProductRequest request) {
    Product product = this.service.findByIdOrThrow(request.getProductId());

    if (
      Objects.nonNull(request.getProductStatusType()) &&
        !request.getProductStatusType().equals(product.getProductStatusType())
    ) {
      product.setProductStatusType(request.getProductStatusType());
    }

    if (
      Objects.nonNull(request.getName()) &&
        !request.getName().equals(product.getName())
    ) {
      this.service.verifyNameAvailability(request.getName());
      product.setName(request.getName());
    }

    if (
      Objects.nonNull(request.getBarCode()) &&
        !request.getBarCode().equals(product.getBarCode())
    ) {
      this.service.verifyBarCodeAvailability(request.getBarCode());
      product.setBarCode(request.getBarCode());
    }

    if (
      Objects.nonNull(request.getPrice()) &&
        !request.getPrice().equals(product.getPrice())
    ) {
      product.setPrice(request.getPrice());
    }

    if (
      Objects.nonNull(request.getStock()) &&
        !request.getStock().equals(product.getStock())
    ) {
      product.setStock(request.getStock());
    }

    if (
      Objects.nonNull(request.getImageUrl()) &&
        !request.getImageUrl().equals(product.getImageUrl())
    ) {
      product.setImageUrl(request.getImageUrl());
    }

    if (
      Objects.nonNull(request.getProductCategoryIds())
    ) {
      List<ProductCategory> categories = this.productCategoryRepository.findAllByProductCategoryIdIn(request.getProductCategoryIds());
      product.setCategories(new HashSet<>(categories));
    }

    return this.repository.save(product);
  }

  public List<Product> getProducts(GetProductsRequest request) {
    return this.repository.findAllByProductIdIn(request.getProductIds());
  }

  public Product getProductById(Long productId) {
    return this.service.findByIdOrThrow(productId);
  }
}

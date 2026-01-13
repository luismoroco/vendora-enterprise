package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.Product;
import com.vendora.backend.application.entity.ProductStatusType;
import com.vendora.backend.application.entity.Provider;
import com.vendora.backend.application.repository.ProductRepository;
import com.vendora.backend.application.repository.ProviderRepository;
import com.vendora.backend.application.usecase.request.CreateProductRequest;
import com.vendora.backend.application.usecase.request.GetProductsRequest;
import com.vendora.backend.application.usecase.request.UpdateProductRequest;
import com.vendora.backend.common.exc.NotFoundException;
import com.vendora.backend.common.exc.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductUseCase {
  private final ProductRepository repository;
  private final ProviderRepository providerRepository;

  public Product createProduct(CreateProductRequest request) {
    Provider provider = this.providerRepository.findById(request.getProviderId())
      .orElseThrow(() -> new NotFoundException("Provider not found"));

    if (this.repository.existsByBarCode(request.getBarCode())) {
      throw new BadRequestException("BarCode already exists");
    }

    return this.repository.save(
      Product.builder()
        .productStatusType(ProductStatusType.ENABLED)
        .name(request.getName())
        .barCode(request.getBarCode())
        .price(request.getPrice())
        .stock(request.getStock())
        .provider(provider)
        .imageUrl(request.getImageUrl())
        .build()
    );
  }

  public Product updateProduct(UpdateProductRequest request) {
    Product product = this.repository.findById(request.getProductId())
      .orElseThrow(() -> new NotFoundException("Product not found"));

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
      product.setName(request.getName());
    }

    if (Objects.nonNull(request.getBarCode())) {
      if (
        !request.getBarCode().equals(product.getBarCode()) &&
          this.repository.countAllByBarCodeIn(List.of(request.getBarCode())) > 0
      ) {
        throw new BadRequestException("BarCode already in use");
      }

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

    return this.repository.save(product);
  }

  public List<Product> getProducts(GetProductsRequest request) {
    return this.repository.findAllByProductIdIn(request.getProductIds());
  }

  public Product getProductById(Long productId) {
    return this.repository.findById(productId)
      .orElseThrow(() -> new NotFoundException("Product not found"));
  }
}

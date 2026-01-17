package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.ProductCategory;
import com.vendora.backend.application.repository.ProductCategoryRepository;
import com.vendora.backend.application.usecase.request.CreateProductCategoryRequest;
import com.vendora.backend.application.usecase.request.GetProductCategoriesRequest;
import com.vendora.backend.application.usecase.request.UpdateProductCategoryRequest;
import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductCategoryUseCase {
  private final ProductCategoryRepository repository;

  public ProductCategory createProductCategory(CreateProductCategoryRequest request) {
    if (this.repository.existsByName(request.getName())) {
      throw new BadRequestException("Product Category already exists");
    }

    return this.repository.save(
      ProductCategory.builder()
        .name(request.getName())
        .imageUrl(request.getImageUrl())
        .featured(request.getFeatured())
        .build()
    );
  }

  public ProductCategory updateProductCategory(UpdateProductCategoryRequest request) {
    ProductCategory productCategory = this.repository.findById(request.getProductCategoryId())
      .orElseThrow(() -> new NotFoundException("Product Category not found"));

    if (
      Objects.nonNull(request.getName()) &&
        !request.getName().equals(productCategory.getName())
    ) {
      productCategory.setName(request.getName());
    }

    if (
      Objects.nonNull(request.getImageUrl()) &&
        !request.getImageUrl().equals(productCategory.getImageUrl())
    ) {
      productCategory.setImageUrl(request.getImageUrl());
    }

    if (
      Objects.nonNull(request.getFeatured()) &&
        !request.getFeatured().equals(productCategory.getFeatured())
    ) {
      productCategory.setFeatured(request.getFeatured());
    }

    return this.repository.save(productCategory);
  }

  public Page<ProductCategory> getProductCategories(GetProductCategoriesRequest request) {
    return this.repository.find(
      Objects.isNull(request.getName()) ? "" : request.getName(),
      request.getProductCategoryIds(),
      PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(Sort.Direction.ASC, "updatedAt"))
    );
  }

  public ProductCategory getProductCategoryById(Long productCategoryId) {
    return this.repository.findById(productCategoryId)
      .orElseThrow(() -> new NotFoundException("Product Category not found"));
  }
}

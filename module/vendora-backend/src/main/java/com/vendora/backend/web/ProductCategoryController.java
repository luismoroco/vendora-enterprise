package com.vendora.backend.web;

import com.vendora.backend.application.entity.ProductCategory;
import com.vendora.backend.application.usecase.ProductCategoryUseCase;
import com.vendora.backend.common.web.api.Paginator;
import com.vendora.backend.web.validator.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/product-category")
@RequiredArgsConstructor
public class ProductCategoryController {
  private final ProductCategoryUseCase useCase;

  private static final int PAGE_SIZE = 20;
  private static final int PAGE = 0;

  @PostMapping("")
  public ResponseEntity<ProductCategory> createProductCategory(@Valid @RequestBody CreateProductCategoryWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(CreateProductCategoryWebRequest::buildRequest)
      .map(this.useCase::createProductCategory)
      .map(productCategory ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(productCategory)
      )
      .findFirst()
      .orElseThrow();
  }

  @PutMapping("/{productCategoryId}")
  public ResponseEntity<ProductCategory> updateBrand(@Valid @RequestBody UpdateProductCategoryWebRequest webRequest, @PathVariable Long productCategoryId) {
    return Stream.of(webRequest)
      .map(request -> request.buildRequest(Map.of("productCategoryId", productCategoryId)))
      .map(this.useCase::updateProductCategory)
      .map(productCategory ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(productCategory)
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("")
  public ResponseEntity<Paginator<ProductCategory>> getProductCategories(@Valid GetProductCategoriesWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(GetProductCategoriesWebRequest::buildRequest)
      .map(this.useCase::getProductCategories)
      .map(productCategories ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(
            Paginator.<ProductCategory>builder()
              .page(PAGE)
              .size(PAGE_SIZE)
              .content(productCategories)
              .build()
          )
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("/{productCategoryId}")
  public ResponseEntity<ProductCategory> getBrandById(@PathVariable Long productCategoryId) {
    return Stream.of(productCategoryId)
      .map(this.useCase::getProductCategoryById)
      .map(productCategory ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(productCategory)
      )
      .findFirst()
      .orElseThrow();
  }
}

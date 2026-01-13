package com.vendora.backend.web;

import com.vendora.backend.application.entity.Product;
import com.vendora.backend.application.usecase.ProductUseCase;
import com.vendora.backend.common.web.api.Paginator;
import com.vendora.backend.web.validator.CreateProductWebRequest;
import com.vendora.backend.web.validator.GetProductsWebRequest;
import com.vendora.backend.web.validator.UpdateProductWebRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductUseCase useCase;

  private static final int PAGE_SIZE = 20;
  private static final int PAGE = 0;

  @PostMapping("")
  public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(CreateProductWebRequest::buildRequest)
      .map(this.useCase::createProduct)
      .map(provider ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(provider)
      )
      .findFirst()
      .orElseThrow();
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Product> updateProduct(@Valid @RequestBody UpdateProductWebRequest webRequest, @PathVariable Long productId) {
    return Stream.of(webRequest)
      .map(request -> request.buildRequest(Map.of("productId", productId)))
      .map(this.useCase::updateProduct)
      .map(product ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(product)
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("")
  public ResponseEntity<Paginator<Product>> getProducts(@Valid GetProductsWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(GetProductsWebRequest::buildRequest)
      .map(this.useCase::getProducts)
      .map(products ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(
            Paginator.<Product>builder()
              .page(PAGE)
              .size(PAGE_SIZE)
              .content(products)
              .build()
          )
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
    return Stream.of(productId)
      .map(this.useCase::getProductById)
      .map(product ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(product)
      )
      .findFirst()
      .orElseThrow();
  }
}

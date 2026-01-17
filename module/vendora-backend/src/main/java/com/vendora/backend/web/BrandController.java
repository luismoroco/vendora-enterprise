package com.vendora.backend.web;

import com.vendora.backend.application.entity.Brand;
import com.vendora.backend.application.usecase.BrandUseCase;
import com.vendora.backend.web.validator.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
  private final BrandUseCase useCase;

  private static final int PAGE_SIZE = 20;
  private static final int PAGE = 0;

  @PostMapping("")
  public ResponseEntity<Brand> createBrand(@Valid @RequestBody CreateBrandWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(CreateBrandWebRequest::buildRequest)
      .map(this.useCase::createBrand)
      .map(brand ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(brand)
      )
      .findFirst()
      .orElseThrow();
  }

  @PutMapping("/{brandId}")
  public ResponseEntity<Brand> updateBrand(@Valid @RequestBody UpdateBrandWebRequest webRequest, @PathVariable Long brandId) {
    return Stream.of(webRequest)
      .map(request -> request.buildRequest(Map.of("brandIds", brandId)))
      .map(this.useCase::updateBrand)
      .map(brand ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(brand)
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("")
  public ResponseEntity<Page<Brand>> getBrands(@Valid GetBrandsWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(GetBrandsWebRequest::buildRequest)
      .map(this.useCase::getBrands)
      .map(brands ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(brands)
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("/{brandId}")
  public ResponseEntity<Brand> getBrandById(@PathVariable Long brandId) {
    return Stream.of(brandId)
      .map(this.useCase::getBrandById)
      .map(brand ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(brand)
      )
      .findFirst()
      .orElseThrow();
  }
}

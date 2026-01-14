package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.Brand;
import com.vendora.backend.application.repository.BrandRepository;
import com.vendora.backend.application.usecase.service.BrandService;
import com.vendora.backend.application.usecase.request.CreateBrandRequest;
import com.vendora.backend.application.usecase.request.GetBrandsRequest;
import com.vendora.backend.application.usecase.request.UpdateBrandRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandUseCase {
  private final BrandRepository repository;
  private final BrandService service;

  public Brand createBrand(CreateBrandRequest request) {
    this.service.verifyNameAvailability(request.getName());

    return this.repository.save(
      Brand.builder()
        .name(request.getName())
        .imageUrl(request.getImageUrl())
        .build()
    );
  }

  public Brand updateBrand(UpdateBrandRequest request) {
    Brand brand = this.service.findByIdOrThrow(request.getBrandId());

    if (
      Objects.nonNull(request.getName()) &&
        !request.getName().equals(brand.getName())
    ) {
      this.service.verifyNameAvailability(request.getName());
      brand.setName(request.getName());
    }

    if (
      Objects.nonNull(request.getImageUrl()) &&
        !request.getImageUrl().equals(brand.getImageUrl())
    ) {
      brand.setImageUrl(request.getImageUrl());
    }

    return this.repository.save(brand);
  }

  public List<Brand> getBrands(GetBrandsRequest request) {
    return this.repository.findAllByBrandIdIn(request.getBrandIds());
  }

  public Brand getBrandById(Long brandId) {
    return this.service.findByIdOrThrow(brandId);
  }
}

package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.Brand;
import com.vendora.backend.application.repository.BrandRepository;
import com.vendora.backend.application.usecase.request.CreateBrandRequest;
import com.vendora.backend.application.usecase.request.GetBrandsRequest;
import com.vendora.backend.application.usecase.request.UpdateBrandRequest;
import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandUseCase {
  private final BrandRepository repository;

  public Brand createBrand(CreateBrandRequest request) {
    if (this.repository.existsByName(request.getName())) {
      throw new BadRequestException("Brand already exists");
    }

    return this.repository.save(
      Brand.builder()
        .name(request.getName())
        .imageUrl(request.getImageUrl())
        .build()
    );
  }

  public Brand updateBrand(UpdateBrandRequest request) {
    Brand brand = this.repository.findById(request.getBrandId())
      .orElseThrow(() -> new NotFoundException("Brand not found"));

    if (
      Objects.nonNull(request.getName()) &&
        !request.getName().equals(brand.getName())
    ) {
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
    return this.repository.findById(brandId)
      .orElseThrow(() -> new NotFoundException("Brand not found"));
  }
}

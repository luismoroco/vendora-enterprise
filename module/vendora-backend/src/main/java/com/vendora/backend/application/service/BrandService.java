package com.vendora.backend.application.service;

import com.vendora.backend.application.entity.Brand;
import com.vendora.backend.application.repository.BrandRepository;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {
  private final BrandRepository repository;

  public Brand findByIdOrThrow(Long brandId) {
    return this.repository.findById(brandId)
      .orElseThrow(() -> new NotFoundException("Brand not found [brandId=%d]".formatted(brandId)));
  }
}

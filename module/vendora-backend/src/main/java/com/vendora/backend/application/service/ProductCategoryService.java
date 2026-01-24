package com.vendora.backend.application.service;

import com.vendora.backend.application.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
  private final ProductCategoryRepository repository;


}

package com.vendora.backend.application.usecase;

import com.vendora.backend.application.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductUseCase {
  private final ProductRepository repository;
}

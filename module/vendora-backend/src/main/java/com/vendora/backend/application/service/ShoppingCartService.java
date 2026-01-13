package com.vendora.backend.application.service;

import com.vendora.backend.application.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
  private final ShoppingCartRepository repository;
}

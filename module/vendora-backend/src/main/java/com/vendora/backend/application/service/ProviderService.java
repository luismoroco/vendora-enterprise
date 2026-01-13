package com.vendora.backend.application.service;

import com.vendora.backend.application.entity.Provider;
import com.vendora.backend.application.repository.ProviderRepository;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService {
  private final ProviderRepository repository;

  public Provider findByIdOrThrow(Long providerId) {
    return this.repository.findById(providerId)
      .orElseThrow(() -> new NotFoundException("Provider not found [providerId=%d]".formatted(providerId)));
  }
}

package com.vendora.backend.application.usecase;

import com.vendora.backend.application.service.ProviderService;
import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.application.entity.Provider;
import com.vendora.backend.application.repository.ProviderRepository;
import com.vendora.backend.application.usecase.request.CreateProviderRequest;
import com.vendora.backend.application.usecase.request.GetProvidersRequest;
import com.vendora.backend.application.usecase.request.UpdateProviderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProviderUseCase {
  private final ProviderRepository repository;
  private final ProviderService service;

  public Provider createProvider(CreateProviderRequest request) {
    if (this.repository.existsByRuc(request.getRuc())) {
      throw new BadRequestException("RUC already in use");
    }

    return this.repository.save(
      Provider.builder()
        .name(request.getName())
        .ruc(request.getRuc())
        .phone(request.getPhone())
        .email(request.getEmail())
        .build()
    );
  }

  public Provider updateProvider(UpdateProviderRequest request) {
    Provider provider = this.service.findByIdOrThrow(request.getProviderId());

    if (Objects.nonNull(request.getName())) {
      provider.setName(request.getName());
    }

    if (Objects.nonNull(request.getRuc())) {
      if (
        !request.getRuc().equals(provider.getRuc()) &&
          this.repository.countAllByRucIn(List.of(request.getRuc())) > 0
      ) {
        throw new BadRequestException("RUC already in use");
      }

      provider.setRuc(request.getRuc());
    }

    if (Objects.nonNull(request.getPhone())) {
      provider.setPhone(request.getPhone());
    }

    if (Objects.nonNull(request.getEmail())) {
      provider.setEmail(request.getEmail());
    }

    return this.repository.save(provider);
  }

  public Page<Provider> getProviders(GetProvidersRequest request) {
    return this.repository.find(
      Objects.isNull(request.getName()) ? "" : request.getName(),
      request.getProviderIds(),
      PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(Sort.Direction.ASC, "updatedAt"))
    );
  }

  public Provider getProviderById(Long providerId) {
    return this.service.findByIdOrThrow(providerId);
  }
}

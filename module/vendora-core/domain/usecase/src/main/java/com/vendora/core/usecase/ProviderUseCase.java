package com.vendora.core.usecase;

import com.vendora.core.model.Provider;
import com.vendora.core.model.gateway.ProviderRepository;
import com.vendora.core.usecase.dto.CreateProviderDTO;
import com.vendora.core.usecase.dto.UpdateProviderDTO;
import com.vendora.core.usecase.service.ProviderService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProviderUseCase {

    private final ProviderRepository repository;
    private final ProviderService service;

    public Mono<Provider> createProvider(CreateProviderDTO dto) {
        return this.service.verifyProviderNameUniquenessWithinTenantOrThrow(dto.getName(), dto.getTenantId())
            .then(this.repository.save(
                Provider.builder()
                    .name(dto.getName())
                    .ruc(dto.getRuc())
                    .phone(dto.getPhone())
                    .email(dto.getEmail())
                    .tenantId(dto.getTenantId())
                    .build()
            ));
    }

    public Mono<Provider> updateProvider(UpdateProviderDTO dto) {
        return this.service.findByProviderIdAndTenantIdOrThrow(dto.getProviderId(), dto.getTenantId())
            .flatMap(provider ->
                Mono.justOrEmpty(dto.getName())
                    .filter(name -> !name.equals(provider.getName()))
                    .flatMap(name ->
                        this.service.verifyProviderNameUniquenessWithinTenantOrThrow(name, dto.getTenantId())
                            .doOnSuccess(__ -> provider.setName(name))
                            .thenReturn(provider)
                    )
                    .defaultIfEmpty(provider)
            )
            .flatMap(provider ->
                Mono.justOrEmpty(dto.getRuc())
                    .filter(ruc -> !ruc.equals(provider.getRuc()))
                    .doOnNext(provider::setRuc)
                    .thenReturn(provider)
                    .defaultIfEmpty(provider)
            )
            .flatMap(provider ->
                Mono.justOrEmpty(dto.getPhone())
                    .filter(phone -> !phone.equals(provider.getPhone()))
                    .doOnNext(provider::setPhone)
                    .thenReturn(provider)
                    .defaultIfEmpty(provider)
            )
            .flatMap(provider ->
                Mono.justOrEmpty(dto.getEmail())
                    .filter(email -> !email.equals(provider.getEmail()))
                    .doOnNext(provider::setEmail)
                    .thenReturn(provider)
                    .defaultIfEmpty(provider)
            )
            .flatMap(this.repository::save);
    }
}

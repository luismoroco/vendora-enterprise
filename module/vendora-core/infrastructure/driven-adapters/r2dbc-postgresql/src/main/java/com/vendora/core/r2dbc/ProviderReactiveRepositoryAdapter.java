package com.vendora.core.r2dbc;

import com.vendora.core.model.Provider;
import com.vendora.core.model.gateway.ProviderRepository;
import com.vendora.core.r2dbc.entity.ProviderEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProviderReactiveRepositoryAdapter extends ReactiveAdapterOperations<Provider, ProviderEntity, Long, ProviderReactiveRepository> implements ProviderRepository {

    protected ProviderReactiveRepositoryAdapter(ProviderReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, providerEntity -> mapper.map(providerEntity, Provider.class));
    }

    @Override
    public Mono<Provider> findByRucAndTenantId(String ruc, Long tenantId) {
        return this.repository.findByRucAndTenantId(ruc, tenantId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByRucAndTenantId(String ruc, Long tenantId) {
        return this.repository.existsByRucAndTenantId(ruc, tenantId);
    }

    @Override
    public Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId) {
        return this.repository.existsByNameAndTenantId(name, tenantId);
    }

    @Override
    public Mono<Provider> findByProviderIdAndTenantId(Long providerId, Long tenantId) {
        return this.repository.findByProviderIdAndTenantId(providerId, tenantId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByProviderIdAndTenantId(Long providerId, Long tenantId) {
        return this.repository.existsByProviderIdAndTenantId(providerId, tenantId);
    }
}


package com.vendora.core.r2dbc;

import com.vendora.core.model.provider.Provider;
import com.vendora.core.model.provider.gateways.ProviderRepository;
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
}


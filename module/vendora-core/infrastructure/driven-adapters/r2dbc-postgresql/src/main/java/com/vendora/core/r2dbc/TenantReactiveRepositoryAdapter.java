package com.vendora.core.r2dbc;

import com.vendora.core.model.Tenant;
import com.vendora.core.model.gateway.TenantRepository;
import com.vendora.core.r2dbc.entity.TenantEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TenantReactiveRepositoryAdapter extends ReactiveAdapterOperations<Tenant, TenantEntity, Long, TenantReactiveRepository> implements TenantRepository {

    protected TenantReactiveRepositoryAdapter(TenantReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, tenantEntity -> mapper.map(tenantEntity, Tenant.class));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return this.repository.existsByName(name);
    }

    @Override
    public Mono<Boolean> existsByDomain(String domain) {
        return null;
    }
}

package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.TenantEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TenantReactiveRepository extends ReactiveCrudRepository<TenantEntity, Long>, ReactiveQueryByExampleExecutor<TenantEntity> {
}

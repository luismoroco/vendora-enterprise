package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmailAndTenantId(String email, Long tenantId);

    Mono<Boolean> existsByEmailAndTenantId(String email, Long tenantId);

    Mono<UserEntity> findByUserIdAndTenantId(Long userId, Long tenantId);

    Mono<Boolean> existsByUserIdAndTenantId(Long userId, Long tenantId);
}


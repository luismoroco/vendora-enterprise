package com.vendora.core.model.gateway;

import com.vendora.core.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User user);

    Mono<User> findByEmailAndTenantId(String email, Long tenantId);

    Mono<Boolean> existsByEmailAndTenantId(String email, Long tenantId);

    Mono<User> findByUserIdAndTenantId(Long userId, Long tenantId);

    Mono<Boolean> existsByUserIdAndTenantId(Long userId, Long tenantId);

    Mono<User> findByUserId(Long userId);
}

package com.vendora.core.usecase.service;

import com.vendora.common.LogCatalog;
import com.vendora.common.exc.BadRequestException;
import com.vendora.common.exc.NotFoundException;
import com.vendora.core.model.User;
import com.vendora.core.model.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Mono<User> findByUserIdAndTenantIdOrThrow(Long userId, Long tenantId) {
        return this.repository.findByUserIdAndTenantId(userId, tenantId)
            .switchIfEmpty(Mono.error(new NotFoundException(LogCatalog.ENTITY_NOT_FOUND.of(User.TYPE))));
    }

    public Mono<Void> requireUniqueUserEmail(String email, Long tenantId) {
        return this.repository.existsByEmailAndTenantId(email, tenantId)
            .flatMap(flag -> flag.equals(Boolean.TRUE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_ALREADY_EXISTS.of(User.TYPE)))
                : Mono.empty()
            );
    }

    public Mono<Void> existsByUserIdAndTenantIdOrThrow(Long userId, Long tenantId) {
        return this.repository.existsByUserIdAndTenantId(userId, tenantId)
            .flatMap(flag -> flag.equals(Boolean.FALSE)
                ? Mono.error(new BadRequestException(LogCatalog.ENTITY_NOT_FOUND.of(User.TYPE)))
                : Mono.empty()
            );
    }
}


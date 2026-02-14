package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.common.exc.NotFoundException;
import com.vendora.core.model.User;
import com.vendora.core.model.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.User.EMAIL;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Mono<User> getByUserIdAndTenantId(Long userId, Long tenantId) {
        return this.repository.findByUserIdAndTenantId(userId, tenantId)
            .switchIfEmpty(Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(User.TYPE))));
    }

    /**
     * Validators
     * */

    public Mono<Void> verifyEmailConstraints(String email, Long tenantId) {
        return ValidatorUtils.email(EMAIL, email)
            .then(this.repository.existsByEmailAndTenantId(email, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(User.TYPE)))
                    : Mono.empty()
                )
            );
    }
}


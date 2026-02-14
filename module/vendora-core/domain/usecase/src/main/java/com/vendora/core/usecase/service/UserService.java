package com.vendora.core.usecase.service;

import com.vendora.common.ValidatorUtils;
import com.vendora.common.exc.BadRequestException;
import com.vendora.common.exc.NotFoundException;
import com.vendora.core.model.User;
import com.vendora.core.model.gateway.UserRepository;
import com.vendora.core.usecase.dto.UpdateUserDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.vendora.common.LogCatalog.ENTITY_ALREADY_EXISTS;
import static com.vendora.common.LogCatalog.ENTITY_NOT_FOUND;
import static com.vendora.core.model.User.EMAIL;
import static com.vendora.core.model.User.FIRST_NAME;
import static com.vendora.core.model.User.LAST_NAME;
import static com.vendora.core.model.User.PASSWORD;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Mono<User> getByUserIdAndTenantId(Long userId, Long tenantId) {
        return this.repository.findByUserIdAndTenantId(userId, tenantId)
            .switchIfEmpty(Mono.error(new NotFoundException(ENTITY_NOT_FOUND.of(User.TYPE))));
    }

    /**
     * DTO-level Validator
     * */

    public Mono<Void> validateDTO(UpdateUserDTO dto) {
        return Mono.when(
            Mono.justOrEmpty(dto.getFirstName())
                .flatMap(UserService::verifyFirstNameConstraints),
            Mono.justOrEmpty(dto.getLastName())
                .flatMap(UserService::verifyLastNameConstraints),
            Mono.justOrEmpty(dto.getEmail())
                .flatMap(email -> this.verifyEmailConstraints(email, dto.getTenantId())),
            Mono.justOrEmpty(dto.getPassword())
                .flatMap(UserService::verifyPasswordConstraints)
        );
    }

    /**
     * Validators
     * */

    public static Mono<Void> verifyFirstNameConstraints(String firstName) {
        return ValidatorUtils.string(FIRST_NAME, firstName);
    }

    public static Mono<Void> verifyLastNameConstraints(String lastName) {
        return ValidatorUtils.string(LAST_NAME, lastName);
    }

    public Mono<Void> verifyEmailConstraints(String email, Long tenantId) {
        return ValidatorUtils.email(EMAIL, email)
            .then(this.repository.existsByEmailAndTenantId(email, tenantId)
                .flatMap(flag -> flag.equals(Boolean.TRUE)
                    ? Mono.error(new BadRequestException(ENTITY_ALREADY_EXISTS.of(User.TYPE)))
                    : Mono.empty()
                )
            );
    }

    public static Mono<Void> verifyPasswordConstraints(String password) {
        return ValidatorUtils.string(PASSWORD, password);
    }
}


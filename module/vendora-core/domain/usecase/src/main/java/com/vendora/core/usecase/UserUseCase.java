package com.vendora.core.usecase;

import com.vendora.core.model.User;
import com.vendora.core.model.gateway.UserRepository;
import com.vendora.core.usecase.dto.UpdateUserDTO;
import com.vendora.core.usecase.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository repository;
    private final UserService service;

    public Mono<User> updateUser(UpdateUserDTO dto) {
        return this.service.findByUserIdAndTenantIdOrThrow(dto.getUserId(), dto.getTenantId())
            .flatMap(user ->
                Mono.justOrEmpty(dto.getEmail())
                    .filter(email -> !email.equals(user.getEmail()))
                    .flatMap(email ->
                        this.service.requireUniqueUserEmail(email, dto.getTenantId())
                            .doOnSuccess(__ -> user.setEmail(email))
                            .thenReturn(user)
                    )
                    .defaultIfEmpty(user)
            )
            .flatMap(user ->
                Mono.justOrEmpty(dto.getFirstName())
                    .filter(firstName -> !firstName.equals(user.getFirstName()))
                    .doOnNext(user::setFirstName)
                    .thenReturn(user)
                    .defaultIfEmpty(user)
            )
            .flatMap(user ->
                Mono.justOrEmpty(dto.getLastName())
                    .filter(lastName -> !lastName.equals(user.getLastName()))
                    .doOnNext(user::setLastName)
                    .thenReturn(user)
                    .defaultIfEmpty(user)
            )
            .flatMap(user ->
                Mono.justOrEmpty(dto.getPassword())
                    .filter(password -> !password.equals(user.getPassword()))
                    .doOnNext(user::setPassword)
                    .thenReturn(user)
                    .defaultIfEmpty(user)
            )
            .flatMap(this.repository::save);
    }
}

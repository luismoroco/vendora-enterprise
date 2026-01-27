package com.vendora.core.api;

import com.vendora.core.usecase.UserUseCase;
import com.vendora.core.usecase.dto.UpdateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase useCase;

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateUserDTO.class)
            .flatMap(this.useCase::updateUser)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }
}


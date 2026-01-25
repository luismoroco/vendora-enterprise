package com.vendora.core.api;

import com.vendora.core.usecase.ProviderUseCase;
import com.vendora.core.usecase.dto.CreateProviderDTO;
import com.vendora.core.usecase.dto.UpdateProviderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProviderHandler {

    private final ProviderUseCase useCase;

    public Mono<ServerResponse> createProvider(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateProviderDTO.class)
            .flatMap(this.useCase::createProvider)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }

    public Mono<ServerResponse> updateProvider(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateProviderDTO.class)
            .flatMap(this.useCase::updateProvider)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }
}


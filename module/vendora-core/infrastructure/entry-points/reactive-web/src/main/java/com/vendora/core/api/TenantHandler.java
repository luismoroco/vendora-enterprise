package com.vendora.core.api;

import com.vendora.core.usecase.TenantUseCase;
import com.vendora.core.usecase.dto.CreateTenantDTO;
import com.vendora.core.usecase.dto.UpdateTenantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TenantHandler {

    private final TenantUseCase useCase;

    public Mono<ServerResponse> createTenant(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateTenantDTO.class)
            .flatMap(this.useCase::createTenant)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }

    public Mono<ServerResponse> updateTenant(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UpdateTenantDTO.class)
            .flatMap(this.useCase::updateTenant)
            .flatMap(dto -> ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
            );
    }
}


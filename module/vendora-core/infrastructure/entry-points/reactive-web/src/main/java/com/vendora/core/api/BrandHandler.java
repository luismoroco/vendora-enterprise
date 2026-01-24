package com.vendora.core.api;

import com.vendora.core.usecase.brand.BrandUseCase;
import com.vendora.core.usecase.brand.dto.CreateBrandDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BrandHandler {

    private final BrandUseCase useCase;

    public Mono<ServerResponse> createBrand(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateBrandDTO.class)
          .flatMap(this.useCase::createBrand)
          .flatMap(dto -> ServerResponse
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
          );
    }
}

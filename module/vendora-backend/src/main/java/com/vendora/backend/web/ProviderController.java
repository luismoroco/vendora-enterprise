package com.vendora.backend.web;

import com.vendora.backend.application.entity.Provider;
import com.vendora.backend.application.usecase.ProviderUseCase;
import com.vendora.backend.common.web.api.Paginator;
import com.vendora.backend.web.validator.CreateProviderWebRequest;
import com.vendora.backend.web.validator.GetProvidersWebRequest;
import com.vendora.backend.web.validator.UpdateProviderWebRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {
  private final ProviderUseCase useCase;

  private static final int PAGE_SIZE = 20;
  private static final int PAGE = 0;

  @PostMapping("")
  public ResponseEntity<Provider> createProvider(@Valid @RequestBody CreateProviderWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(CreateProviderWebRequest::buildRequest)
      .map(this.useCase::createProvider)
      .map(provider ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(provider)
      )
      .findFirst()
      .orElseThrow();
  }

  @PutMapping("/{providerId}")
  public ResponseEntity<Provider> updateProvider(@Valid @RequestBody UpdateProviderWebRequest webRequest, @PathVariable Long providerId) {
    return Stream.of(webRequest)
      .map(request -> request.buildRequest(Map.of("providerId", providerId)))
      .map(this.useCase::updateProvider)
      .map(provider ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(provider)
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("")
  public ResponseEntity<Paginator<Provider>> getProviders(@Valid GetProvidersWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(GetProvidersWebRequest::buildRequest)
      .map(this.useCase::getProviders)
      .map(providers ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(
            Paginator.<Provider>builder()
              .page(PAGE)
              .size(PAGE_SIZE)
              .content(providers)
              .build()
          )
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("/{providerId}")
  public ResponseEntity<Provider> getProvider(@PathVariable Long providerId) {
    return Stream.of(providerId)
      .map(this.useCase::getProviderById)
      .map(provider ->
        ResponseEntity
          .status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(provider)
      )
      .findFirst()
      .orElseThrow();
  }
}

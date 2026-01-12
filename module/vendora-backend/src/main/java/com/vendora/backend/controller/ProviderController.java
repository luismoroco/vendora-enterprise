package com.vendora.backend.controller;

import com.vendora.backend.entity.Provider;
import com.vendora.backend.usecase.ProviderUseCase;
import com.vendora.backend.usecase.request.CreateProviderRequest;
import com.vendora.backend.usecase.request.UpdateProviderRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {
  private final ProviderUseCase useCase;

  @PostMapping("")
  public ResponseEntity<Provider> createProvider(@RequestBody CreateProviderRequest request) throws BadRequestException {
    Provider provider = this.useCase.createProvider(request);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .contentType(MediaType.APPLICATION_JSON)
      .body(provider);
  }

  @PutMapping("/{providerId}")
  public ResponseEntity<Provider> updateProvider(@RequestBody UpdateProviderRequest request, @PathVariable Long providerId) throws BadRequestException {
    Provider provider = this.useCase.updateProvider(request);

    return ResponseEntity
      .status(HttpStatus.OK)
      .contentType(MediaType.APPLICATION_JSON)
      .body(provider);
  }





}

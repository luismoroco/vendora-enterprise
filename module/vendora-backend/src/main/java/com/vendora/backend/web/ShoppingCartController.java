package com.vendora.backend.web;

import com.vendora.backend.application.usecase.ShoppingCartUseCase;
import com.vendora.backend.web.dto.ShoppingCartDto;
import com.vendora.backend.web.validator.UpdateShoppingCartWebRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
  private final ShoppingCartUseCase useCase;

  private static final long SHOPPING_CART_ID = 1;

  @PutMapping("")
  public ResponseEntity<ShoppingCartDto> updateShoppingCart(@Valid @RequestBody UpdateShoppingCartWebRequest webRequest) {
    return Stream.of(webRequest)
      .map(request -> request.buildRequest(Map.of("shoppingCartId", SHOPPING_CART_ID)))
      .map(this.useCase::updateShoppingCart)
      .map(shoppingCart ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(ShoppingCartDto.from(shoppingCart))
      )
      .findFirst()
      .orElseThrow();
  }

  @GetMapping("")
  public ResponseEntity<ShoppingCartDto> getShoppingCart() {
    return Stream.of(SHOPPING_CART_ID)
      .map(this.useCase::getShoppingCartById)
      .map(shoppingCart ->
        ResponseEntity
          .status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(ShoppingCartDto.from(shoppingCart))
      )
      .findFirst()
      .orElseThrow();
  }
}

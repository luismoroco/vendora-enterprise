package com.vendora.backend.application.usecase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShoppingCartRequest {
  private Long shoppingCartId;
  private List<Item> items;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {
    private Long productId;
    private Integer quantity;
  }
}

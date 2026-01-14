package com.vendora.backend.web.dto;

import com.vendora.backend.application.entity.ShoppingCart;
import com.vendora.backend.application.entity.ShoppingCartItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDto {
  private Long shoppingCartId;
  private List<ItemDto> items;
  private Double total;

  public static ShoppingCartDto from(ShoppingCart cart) {
    List<ItemDto> items = cart.getItems().stream()
      .map(ItemDto::from)
      .toList();

    double total = items.stream()
      .mapToDouble(ItemDto::getSubtotal)
      .sum();

    return ShoppingCartDto.builder()
      .shoppingCartId(cart.getShoppingCartId())
      .items(items)
      .total(total)
      .build();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    private String imageUrl;

    public static ItemDto from(ShoppingCartItem item) {
      return ItemDto.builder()
        .productId(item.getProduct().getProductId())
        .productName(item.getProduct().getName())
        .quantity(item.getQuantity())
        .unitPrice(item.getUnitPrice())
        .subtotal(item.getQuantity() * item.getUnitPrice())
        .imageUrl(item.getProduct().getImageUrl())
        .build();
    }
  }
}

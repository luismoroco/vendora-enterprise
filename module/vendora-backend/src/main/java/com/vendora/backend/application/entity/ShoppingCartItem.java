package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "shopping_cart_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long shoppingCartItemId;

  private Integer quantity;
  private Double unitPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  private ShoppingCart cart;
}

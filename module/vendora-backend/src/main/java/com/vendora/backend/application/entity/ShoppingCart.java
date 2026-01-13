package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shopping_cart")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long shoppingCartId;

  @OneToMany(
    mappedBy = "cart",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<ShoppingCartItem> items = new ArrayList<>();
}

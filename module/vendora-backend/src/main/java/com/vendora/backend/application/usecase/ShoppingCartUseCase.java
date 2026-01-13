package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.Product;
import com.vendora.backend.application.entity.ShoppingCart;
import com.vendora.backend.application.entity.ShoppingCartItem;
import com.vendora.backend.application.repository.ShoppingCartRepository;
import com.vendora.backend.application.service.ProductService;
import com.vendora.backend.application.usecase.request.UpdateShoppingCartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ShoppingCartUseCase {
  private final ShoppingCartRepository repository;
  private final ProductService productService;

  public ShoppingCart updateShoppingCart(UpdateShoppingCartRequest request) {
    ShoppingCart shoppingCart = this.repository.findById(request.getShoppingCartId())
      .orElseGet(ShoppingCart::new);

    var items = request.getItems().stream()
      .map(item -> {
        Product product = this.productService.findByIdOrThrow(item.getProductId());
        this.productService.verifyPurchasable(product, item.getQuantity());

        return ShoppingCartItem.builder()
          .quantity(item.getQuantity())
          .unitPrice(product.getPrice())
          .product(product)
          .cart(shoppingCart)
          .build();
      })
      .toList();

    shoppingCart.setItems(new HashSet<>(items));

    return this.repository.save(shoppingCart);
  }

  public ShoppingCart getShoppingCartById(Long shoppingCartId) {
    return this.repository.findById(shoppingCartId)
      .orElseGet(() -> this.repository.save(new ShoppingCart()));
  }
}

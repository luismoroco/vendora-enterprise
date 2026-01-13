package com.vendora.backend.application.usecase;

import com.vendora.backend.application.entity.Product;
import com.vendora.backend.application.entity.ProductStatusType;
import com.vendora.backend.application.entity.ShoppingCart;
import com.vendora.backend.application.entity.ShoppingCartItem;
import com.vendora.backend.application.repository.ProductRepository;
import com.vendora.backend.application.repository.ShoppingCartRepository;
import com.vendora.backend.application.usecase.request.UpdateShoppingCartRequest;
import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartUseCase {
  private final ShoppingCartRepository repository;
  private final ProductRepository productRepository;

  public ShoppingCart updateShoppingCart(UpdateShoppingCartRequest request) {
    ShoppingCart shoppingCart = this.repository.findById(request.getShoppingCartId())
      .orElseGet(ShoppingCart::new);

    shoppingCart.getItems().clear();
    Set<ShoppingCartItem> items = request.getItems().stream()
      .map(item -> {
        Product product = this.productRepository.findById(item.getProductId())
          .orElseThrow(() -> new NotFoundException("Product not found"));

        if (
          !product.getProductStatusType().equals(ProductStatusType.ENABLED) ||
            product.getStock().equals(0) ||
            product.getStock() < item.getQuantity()
        ) {
          throw new BadRequestException("Product not available");
        }

        return ShoppingCartItem.builder()
          .quantity(item.getQuantity())
          .unitPrice(product.getPrice())
          .product(product)
          .cart(shoppingCart)
          .build();
      })
      .collect(Collectors.toSet());

    shoppingCart.setItems(items);

    return this.repository.save(shoppingCart);
  }

  public ShoppingCart getShoppingCartById(Long shoppingCartId) {
    return this.repository.findById(shoppingCartId)
      .orElseGet(() -> this.repository.save(new ShoppingCart()));
  }
}

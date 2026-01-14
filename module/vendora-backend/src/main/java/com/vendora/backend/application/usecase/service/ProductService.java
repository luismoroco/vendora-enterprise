package com.vendora.backend.application.usecase.service;

import com.vendora.backend.application.entity.Product;
import com.vendora.backend.application.entity.ProductStatusType;
import com.vendora.backend.application.repository.ProductRepository;
import com.vendora.backend.common.exc.BadRequestException;
import com.vendora.backend.common.exc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository repository;

  private static final int ZERO = 0;
  private static final List<ProductStatusType> PURCHASABLE_STATUSES = List.of(ProductStatusType.ENABLED);

  public Product findByIdOrThrow(Long productId) {
    return this.repository.findById(productId)
      .orElseThrow(() -> new NotFoundException("Provider not found [productId=%d]".formatted(productId)));
  }

  public void verifyBarCodeAvailability(String barCode) {
    if (
      this.repository.countAllByBarCodeIn(List.of(barCode)) > ZERO
    ) {
      throw new BadRequestException("BarCode already exists [barCode=%s]".formatted(barCode));
    }
  }

  public void verifyNameAvailability(String name) {
    if (
      this.repository.countAllByNameIn(List.of(name)) > ZERO
    ) {
      throw new BadRequestException("Product name already exists [productName=%s]".formatted(name));
    }
  }

  public void verifyPurchasable(Product product, Integer quantity) {
    if (
      !PURCHASABLE_STATUSES.contains(product.getProductStatusType()) ||
        product.getStock() < quantity
    ) {
      throw new BadRequestException(
        "Product not purchasable [productName=%s][stock=%d]"
          .formatted(product.getName(), product.getStock())
      );
    }
  }

  public Page<Product> find(
    String name,
    String barCode,
    List<Long> categoryIds,
    List<Long> brandIds,
    List<Long> providerIds,
    List<Long> productIds,
    Double minPrice,
    Double maxPrice,
    Integer page,
    Integer size
  ) {
    return this.repository.find(
      Objects.isNull(name) ? "" : name,
      barCode,
      categoryIds,
      brandIds,
      providerIds,
      productIds,
      minPrice,
      maxPrice,
      PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"))
    );
  }
}

package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByBarCode(String barCode);

  Integer countAllByBarCodeIn(List<String> barCodes);

  List<Product> findAllByProductIdIn(List<Long> productIds);
}

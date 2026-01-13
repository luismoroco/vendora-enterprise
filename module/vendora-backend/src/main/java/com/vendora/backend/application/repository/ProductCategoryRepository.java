package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
  List<ProductCategory> findAllByProductCategoryIdIn(List<Long> productCategoryIds);

  boolean existsByName(String name);
}

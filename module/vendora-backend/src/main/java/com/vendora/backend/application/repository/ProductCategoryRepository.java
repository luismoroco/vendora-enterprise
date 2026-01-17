package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
  List<ProductCategory> findAllByProductCategoryIdIn(List<Long> productCategoryIds);

  boolean existsByName(String name);

  @Query(
    "SELECT pc FROM ProductCategory pc " +
      "WHERE (:name IS NULL OR LOWER(pc.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
      "AND (:productCategoryIds IS NULL OR pc.productCategoryId IN :productCategoryIds)")
  Page<ProductCategory> find(
    @Param("name") String name,
    @Param("productCategoryIds") List<Long> productCategoryIds,
    final Pageable pageable
  );
}

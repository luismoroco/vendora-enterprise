package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByBarCode(String barCode);

  Integer countAllByBarCodeIn(List<String> barCodes);

  List<Product> findAllByProductIdIn(List<Long> productIds);

  Integer countAllByNameIn(List<String> names);

  @Query(
    "SELECT p FROM Product p " +
      "LEFT JOIN p.categories c " +
      "LEFT JOIN p.brand b " +
      "LEFT JOIN p.provider pr " +
      "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
      "AND (:categoryIds IS NULL OR c.productCategoryId IN :categoryIds)" +
      "AND (:brandIds IS NULL OR b.brandId IN :brandIds)" +
      "AND (:providerIds IS NULL OR pr.providerId IN :providerIds)" +
      "AND (:barCode IS NULL OR p.barCode = :barCode)" +
      "AND (:minPrice IS NULL OR p.price > :minPrice)" +
      "AND (:maxPrice IS NULL OR p.price < :maxPrice)")
  Page<Product> find(
    @Param("name") String name,
    @Param("barCode") String barCode,
    @Param("categoryIds") List<Long> categoryIds,
    @Param("brandIds") List<Long> brandIds,
    @Param("providerIds") List<Long> providerIds,
    @Param("productIds") List<Long> productIds,
    @Param("minPrice") Double minPrice,
    @Param("maxPrice") Double maxPrice,
    final Pageable pageable
  );
}

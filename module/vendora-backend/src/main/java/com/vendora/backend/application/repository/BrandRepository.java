package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
  boolean existsByName(String name);

  List<Brand> findAllByBrandIdIn(List<Long> brandIds);

  @Query(
    "SELECT b FROM Brand b " +
      "WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
      "AND (:brandIds IS NULL OR b.brandId IN :brandIds)")
  Page<Brand> find(
    @Param("name") String name,
    @Param("brandIds") List<Long> brandIds,
    final Pageable pageable
  );
}

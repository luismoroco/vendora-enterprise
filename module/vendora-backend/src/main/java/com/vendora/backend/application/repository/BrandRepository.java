package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
  boolean existsByName(String name);

  List<Brand> findAllByBrandIdIn(List<Long> brandIds);
}

package com.vendora.backend.application.repository;

import com.vendora.backend.application.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  boolean existsByRuc(String ruc);

  Integer countAllByRucIn(List<String> rucs);

  List<Provider> findAllByProviderIdIn(List<Long> providerIds);

  @Query(
    "SELECT p FROM Provider p " +
      "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
      "AND (:providerIds IS NULL OR p.providerId IN :providerIds)")
  Page<Provider> find(
    @Param("name") String name,
    @Param("providerIds") List<Long> providerIds,
    final Pageable pageable
  );
}

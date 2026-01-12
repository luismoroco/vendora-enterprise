package com.vendora.backend.repository;

import com.vendora.backend.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  boolean existsByRuc(String ruc);

  Integer countAllByRucIn(List<String> rucs);

  List<Provider> findAllByProviderIdIn(List<Long> providerIds);
}

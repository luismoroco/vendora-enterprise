package com.vendora.core.r2dbc;

import com.vendora.core.model.Brand;
import com.vendora.core.model.gateway.BrandRepository;
import com.vendora.core.r2dbc.entity.BrandEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class BrandReactiveRepositoryAdapter extends ReactiveAdapterOperations<Brand, BrandEntity, Long, BrandReactiveRepository> implements BrandRepository {

  protected BrandReactiveRepositoryAdapter(BrandReactiveRepository repository, ObjectMapper mapper) {
      super(repository, mapper, brandEntity -> mapper.map(brandEntity, Brand.class));
  }

  @Override
  public Mono<Brand> findByNameAndTenantId(String name, Long tenantId) {
    return this.repository.findByNameAndTenantId(name, tenantId).map(this::toEntity);
  }

  @Override
  public Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId) {
    return this.repository.existsByNameAndTenantId(name, tenantId);
  }

  @Override
  public Mono<Brand> findByBrandIdAndTenantId(Long brandId, Long tenantId) {
    return this.repository.findByBrandIdAndTenantId(brandId, tenantId).map(this::toEntity);
  }

  @Override
  public Mono<Boolean> existsByBrandIdAndTenantId(Long brandId, Long tenantId) {
    return this.repository.existsByBrandIdAndTenantId(brandId, tenantId);
  }

  @Override
  public Mono<Brand> findByBrandId(Long brandId) {
    return this.repository.findById(brandId).map(this::toEntity);
  }

  @Override
  public Flux<Brand> findByBrandIdIn(List<Long> brandIds) {
    return this.repository.findAllById(brandIds).map(this::toEntity);
  }
}

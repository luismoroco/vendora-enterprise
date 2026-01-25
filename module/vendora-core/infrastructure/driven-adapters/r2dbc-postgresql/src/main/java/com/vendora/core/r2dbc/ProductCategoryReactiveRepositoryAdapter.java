package com.vendora.core.r2dbc;

import com.vendora.core.model.ProductCategory;
import com.vendora.core.model.gateway.ProductCategoryRepository;
import com.vendora.core.r2dbc.entity.ProductCategoryEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductCategoryReactiveRepositoryAdapter extends ReactiveAdapterOperations<ProductCategory, ProductCategoryEntity, Long, ProductCategoryReactiveRepository> implements ProductCategoryRepository {

    protected ProductCategoryReactiveRepositoryAdapter(ProductCategoryReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, productCategoryEntity -> mapper.map(productCategoryEntity, ProductCategory.class));
    }

    @Override
    public Mono<ProductCategory> findByNameAndTenantId(String name, Long tenantId) {
        return this.repository.findByNameAndTenantId(name, tenantId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId) {
        return this.repository.existsByNameAndTenantId(name, tenantId);
    }

    @Override
    public Mono<ProductCategory> findByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId) {
        return this.repository.findByProductCategoryIdAndTenantId(productCategoryId, tenantId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByProductCategoryIdAndTenantId(Long productCategoryId, Long tenantId) {
        return this.repository.existsByProductCategoryIdAndTenantId(productCategoryId, tenantId);
    }

    @Override
    public Mono<ProductCategory> findByProductCategoryId(Long productCategoryId) {
        return this.repository.findById(productCategoryId).map(this::toEntity);
    }
}


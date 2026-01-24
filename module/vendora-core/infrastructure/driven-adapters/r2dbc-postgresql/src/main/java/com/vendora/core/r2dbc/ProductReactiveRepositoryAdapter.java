package com.vendora.core.r2dbc;

import com.vendora.core.model.Product;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.r2dbc.entity.ProductEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<Product, ProductEntity, Long, ProductReactiveRepository> implements ProductRepository {

    protected ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, productEntity -> mapper.map(productEntity, Product.class));
    }

    @Override
    public Mono<Product> findByBarCodeAndTenantId(String barCode, Long tenantId) {
        return this.repository.findByBarCodeAndTenantId(barCode, tenantId).map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId) {
        return this.repository.existsByBarCodeAndTenantId(barCode, tenantId);
    }
}


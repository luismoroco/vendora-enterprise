package com.vendora.core.r2dbc;

import com.vendora.common.EnumUtils;
import com.vendora.common.Paginator;
import com.vendora.core.model.Product;
import com.vendora.core.model.ProductStatusType;
import com.vendora.core.model.gateway.ProductRepository;
import com.vendora.core.r2dbc.entity.ProductEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<Boolean> existsByProductNameAndTenantId(String productName, Long tenantId) {
        return this.repository.existsByNameAndTenantId(productName, tenantId);
    }

    @Override
    public Mono<Product> findByProductIdAndTenantId(Long productId, Long tenantId) {
        return this.repository.findByProductIdAndTenantId(productId, tenantId).map(this::toEntity);
    }

    @Override
    public Flux<Product> findProducts(
        Long tenantId,
        String name,
        String barCode,
        List<Long> providerIds,
        List<Long> brandIds,
        List<Long> categoryIds,
        List<ProductStatusType> productStatusTypes,
        Integer page,
        Integer pageSize
    ) {
        return this.repository.findProducts(
            tenantId,
            name == null ? "" : name,
            categoryIds == null || categoryIds.isEmpty() ? new Long[0] : categoryIds.toArray(new Long[0]),
            brandIds == null || brandIds.isEmpty() ? new Long[0] : brandIds.toArray(new Long[0]),
            providerIds == null || providerIds.isEmpty() ? new Long[0] : providerIds.toArray(new Long[0]),
            barCode,
            productStatusTypes == null || productStatusTypes.isEmpty()
                ? new String[0]
                : EnumUtils.names(productStatusTypes).toArray(new String[0]),
            pageSize,
            Paginator.getOffset(page, pageSize)
        ).map(this::toEntity);
    }

    @Override
    public Mono<Integer> countProducts(
        Long tenantId,
        String name,
        String barCode,
        List<Long> providerIds,
        List<Long> brandIds,
        List<Long> categoryIds,
        List<ProductStatusType> productStatusTypes
    ) {
        return this.repository.countProducts(
            tenantId,
            name == null ? "" : name,
            categoryIds == null || categoryIds.isEmpty() ? new Long[0] : categoryIds.toArray(new Long[0]),
            brandIds == null || brandIds.isEmpty() ? new Long[0] : brandIds.toArray(new Long[0]),
            providerIds == null || providerIds.isEmpty() ? new Long[0] : providerIds.toArray(new Long[0]),
            barCode,
            productStatusTypes == null || productStatusTypes.isEmpty()
                ? new String[0]
                : EnumUtils.names(productStatusTypes).toArray(new String[0])
        );
    }
}


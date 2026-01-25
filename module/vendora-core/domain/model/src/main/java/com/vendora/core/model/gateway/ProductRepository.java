package com.vendora.core.model.gateway;

import com.vendora.core.model.Product;
import com.vendora.core.model.ProductStatusType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository {

    Mono<Product> save(Product product);

    Mono<Product> findByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByProductNameAndTenantId(String productName, Long tenantId);

    Mono<Product> findByProductIdAndTenantId(Long productId, Long tenantId);

    Flux<Product> findProducts(
        Long tenantId,
        String name,
        String barCode,
        List<Long> providerIds,
        List<Long> brandIds,
        List<Long> categoryIds,
        List<ProductStatusType> productStatusTypes,
        Integer page,
        Integer pageSize
    );

    Mono<Integer> countProducts(
        Long tenantId,
        String name,
        String barCode,
        List<Long> providerIds,
        List<Long> brandIds,
        List<Long> categoryIds,
        List<ProductStatusType> productStatusTypes
    );
}

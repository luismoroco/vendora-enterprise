package com.vendora.core.model.product.gateways;

import com.vendora.core.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> save(Product product);

    Mono<Product> findByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId);
}

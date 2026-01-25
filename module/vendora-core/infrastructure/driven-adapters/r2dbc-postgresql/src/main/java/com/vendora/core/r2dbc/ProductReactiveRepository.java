package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, Long>, ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<ProductEntity> findByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<ProductEntity> findByProductIdAndTenantId(Long productId, Long tenantId);

    @Query(
        "SELECT DISTINCT p.* FROM product p " +
        "LEFT JOIN product_category_product pcp ON p.product_id = pcp.product_id " +
        "WHERE p.tenant_id = :tenantId " +
        "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (:categoryIds IS NULL OR pcp.product_category_id = ANY(:categoryIds)) " +
        "AND (:brandIds IS NULL OR p.brand_id = ANY(:brandIds)) " +
        "AND (:providerIds IS NULL OR p.provider_id = ANY(:providerIds)) " +
        "AND (:barCode IS NULL OR p.bar_code = :barCode) " +
        "AND (:productStatusTypes IS NULL OR p.product_status_type = ANY(:productStatusTypes)) " +
        "ORDER BY p.product_id " +
        "LIMIT COALESCE(:pageSize, 2147483647) OFFSET COALESCE(:offset, 0)"
    )
    Flux<ProductEntity> findProducts(
        @Param("tenantId") Long tenantId,
        @Param("name") String name,
        @Param("categoryIds") List<Long> categoryIds,
        @Param("brandIds") List<Long> brandIds,
        @Param("providerIds") List<Long> providerIds,
        @Param("barCode") String barCode,
        @Param("productStatusTypes") List<String> productStatusTypes,
        @Param("pageSize") Integer pageSize,
        @Param("offset") Integer offset
    );

    @Query(
        "SELECT COUNT(p.product_id) FROM product p " +
        "LEFT JOIN product_category_product pcp ON p.product_id = pcp.product_id " +
        "WHERE p.tenant_id = :tenantId " +
        "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (:categoryIds IS NULL OR pcp.product_category_id = ANY(:categoryIds)) " +
        "AND (:brandIds IS NULL OR p.brand_id = ANY(:brandIds)) " +
        "AND (:providerIds IS NULL OR p.provider_id = ANY(:providerIds)) " +
        "AND (:barCode IS NULL OR p.bar_code = :barCode) " +
        "AND (:productStatusTypes IS NULL OR p.product_status_type = ANY(:productStatusTypes))"
    )
    Mono<Integer> countProducts(
        @Param("tenantId") Long tenantId,
        @Param("name") String name,
        @Param("categoryIds") List<Long> categoryIds,
        @Param("brandIds") List<Long> brandIds,
        @Param("providerIds") List<Long> providerIds,
        @Param("barCode") String barCode,
        @Param("productStatusTypes") List<String> productStatusTypes
    );
}


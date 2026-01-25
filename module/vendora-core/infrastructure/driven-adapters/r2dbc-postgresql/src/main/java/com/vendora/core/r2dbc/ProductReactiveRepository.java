package com.vendora.core.r2dbc;

import com.vendora.core.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, Long>, ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<ProductEntity> findByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByBarCodeAndTenantId(String barCode, Long tenantId);

    Mono<Boolean> existsByNameAndTenantId(String name, Long tenantId);

    Mono<ProductEntity> findByProductIdAndTenantId(Long productId, Long tenantId);

    @Query(
        "SELECT DISTINCT p.* FROM product p " +
        "LEFT JOIN product_category_product pcp ON p.product_id = pcp.product_id " +
        "WHERE p.tenant_id = :tenantId " +
        "AND (COALESCE(:name, '') = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (COALESCE(array_length(:categoryIds, 1), 0) = 0 OR pcp.product_category_id = ANY(:categoryIds)) " +
        "AND (COALESCE(array_length(:brandIds, 1), 0) = 0 OR p.brand_id = ANY(:brandIds)) " +
        "AND (COALESCE(array_length(:providerIds, 1), 0) = 0 OR p.provider_id = ANY(:providerIds)) " +
        "AND (:barCode IS NULL OR p.bar_code = :barCode) " +
        "AND (COALESCE(array_length(:productStatusTypes, 1), 0) = 0 OR p.product_status_type = ANY(:productStatusTypes)) " +
        "LIMIT :pageSize OFFSET :offset"
    )
    Flux<ProductEntity> findProducts(
        @Param("tenantId") Long tenantId,
        @Param("name") String name,
        @Param("categoryIds") Long[] categoryIds,
        @Param("brandIds") Long[] brandIds,
        @Param("providerIds") Long[] providerIds,
        @Param("barCode") String barCode,
        @Param("productStatusTypes") String[] productStatusTypes,
        @Param("pageSize") Integer pageSize,
        @Param("offset") Integer offset
    );

    @Query(
        "SELECT COUNT(DISTINCT p.product_id) FROM product p " +
        "LEFT JOIN product_category_product pcp ON p.product_id = pcp.product_id " +
        "WHERE p.tenant_id = :tenantId " +
        "AND (COALESCE(:name, '') = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (COALESCE(array_length(:categoryIds, 1), 0) = 0 OR pcp.product_category_id = ANY(:categoryIds)) " +
        "AND (COALESCE(array_length(:brandIds, 1), 0) = 0 OR p.brand_id = ANY(:brandIds)) " +
        "AND (COALESCE(array_length(:providerIds, 1), 0) = 0 OR p.provider_id = ANY(:providerIds)) " +
        "AND (:barCode IS NULL OR p.bar_code = :barCode) " +
        "AND (COALESCE(array_length(:productStatusTypes, 1), 0) = 0 OR p.product_status_type = ANY(:productStatusTypes))"
    )
    Mono<Integer> countProducts(
        @Param("tenantId") Long tenantId,
        @Param("name") String name,
        @Param("categoryIds") Long[] categoryIds,
        @Param("brandIds") Long[] brandIds,
        @Param("providerIds") Long[] providerIds,
        @Param("barCode") String barCode,
        @Param("productStatusTypes") String[] productStatusTypes
    );
}


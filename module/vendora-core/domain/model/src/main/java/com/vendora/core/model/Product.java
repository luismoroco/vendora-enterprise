package com.vendora.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    public static final String TYPE = "Product";

    public static final String PRODUCT_ID = "productId";
    public static final String PROVIDER_ID = "providerId";
    public static final String NAME = "name";
    public static final String BAR_CODE = "barCode";
    public static final String PRICE = "price";
    public static final String STOCK = "stock";
    public static final String PRODUCT_STATUS_TYPE = "productStatusType";
    public static final String IMAGE_URL = "imageUrl";
    public static final String TENANT_ID = "tenantId";
    public static final String COST = "cost";
    public static final String DESCRIPTION = "description";
    public static final String BRAND_ID = "brandId";

    private Long              productId;
    private Long              providerId;
    private String            name;
    private String            barCode;
    private BigDecimal        price;
    private Integer           stock;
    private ProductStatusType productStatusType;
    private String            imageUrl;
    private Long              tenantId;
    private BigDecimal        cost;
    private String            description;
    private Long              brandId;

    private Provider          provider;
    private Brand             brand;
}

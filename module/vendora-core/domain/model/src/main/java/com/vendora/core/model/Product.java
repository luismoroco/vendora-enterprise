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

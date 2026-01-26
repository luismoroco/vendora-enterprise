package com.vendora.core.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {

    @Id
    private Long        productId;
    private Long        providerId;
    private String      name;
    private String      barCode;
    private BigDecimal  price;
    private Integer     stock;
    private String      productStatusType;
    private String      imageUrl;
    private Long        tenantId;
    private BigDecimal  cost;
    private String      description;
    private Long        brandId;

    @Transient
    private ProviderEntity provider;

    @Transient
    private BrandEntity brand;
}


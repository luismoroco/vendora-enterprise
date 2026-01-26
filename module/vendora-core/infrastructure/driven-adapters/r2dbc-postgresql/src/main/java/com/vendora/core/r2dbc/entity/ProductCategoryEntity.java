package com.vendora.core.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("product_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductCategoryEntity {

    @Id
    private Long    productCategoryId;
    private String  name;
    private Boolean featured;
    private String  imageUrl;
    private Long    tenantId;
}


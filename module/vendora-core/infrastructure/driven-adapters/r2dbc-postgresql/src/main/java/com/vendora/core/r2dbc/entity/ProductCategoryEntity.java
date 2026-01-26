package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
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

    @Id      private Long    productCategoryId;
    @NotNull private String  name;
    @NotNull private Boolean featured;
    @NotNull private String  imageUrl;
    @NotNull private Long    tenantId;
}


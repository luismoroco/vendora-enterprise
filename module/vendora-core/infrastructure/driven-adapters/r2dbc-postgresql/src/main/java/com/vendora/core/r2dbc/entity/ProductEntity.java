package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
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

    @Id        private Long           productId;
    @NotNull   private Long           providerId;
    @NotNull   private String         name;
    @NotNull   private String         barCode;
    @NotNull   private BigDecimal     price;
    @NotNull   private Integer        stock;
    @NotNull   private String         productStatusType;
    @NotNull   private String         imageUrl;
    @NotNull   private Long           tenantId;
    @NotNull   private BigDecimal     cost;
    @NotNull   private String         description;
    @NotNull   private Long           brandId;

    @Transient private ProviderEntity provider;
    @Transient private BrandEntity    brand;
}


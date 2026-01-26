package com.vendora.core.usecase.dto;

import com.vendora.core.model.ProductStatusType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class UpdateProductDTO {

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
}

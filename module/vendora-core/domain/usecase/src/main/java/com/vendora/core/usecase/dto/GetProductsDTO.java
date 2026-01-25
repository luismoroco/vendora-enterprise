package com.vendora.core.usecase.dto;

import com.vendora.core.model.ProductStatusType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class GetProductsDTO {

    private Long tenantId;
    private String name;
    private List<ProductStatusType> productStatusTypes;
    private String barCode;
    private List<Long> providerIds;
    private List<Long> brandIds;
    private List<Long> categoryIds;
    private Integer page;
    private Integer pageSize;
}

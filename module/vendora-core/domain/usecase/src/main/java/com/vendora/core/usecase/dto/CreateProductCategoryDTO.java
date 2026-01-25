package com.vendora.core.usecase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CreateProductCategoryDTO {

    private Long    tenantId;
    private String  name;
    private Boolean featured;
    private String  imageUrl;
}


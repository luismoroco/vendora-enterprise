package com.vendora.core.usecase.brand.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CreateBrandDTO {

    private Long   tenantId;
    private String name;
    private String imageUrl;
}

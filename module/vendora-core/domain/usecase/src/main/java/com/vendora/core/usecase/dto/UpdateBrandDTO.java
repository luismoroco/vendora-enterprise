package com.vendora.core.usecase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class UpdateBrandDTO {

    private Long   brandId;
    private Long   tenantId;
    private String name;
    private String imageUrl;
}

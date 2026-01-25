package com.vendora.core.usecase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CreateProviderDTO {

    private Long   tenantId;
    private String name;
    private String ruc;
    private String phone;
    private String email;
}

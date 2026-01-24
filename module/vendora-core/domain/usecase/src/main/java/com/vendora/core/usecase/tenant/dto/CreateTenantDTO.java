package com.vendora.core.usecase.tenant.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CreateTenantDTO {

    private String name;
    private String domain;
}

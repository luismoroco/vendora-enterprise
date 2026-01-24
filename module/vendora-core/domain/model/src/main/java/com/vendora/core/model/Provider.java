package com.vendora.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Provider {

    private Long   providerId;
    private String name;
    private String ruc;
    private String phone;
    private String email;
    private Long   tenantId;
}

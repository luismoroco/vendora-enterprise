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
public class Tenant {

    public static final String TYPE = "Tenant";

    private Long   tenantId;
    private String name;
    private String domain;
}

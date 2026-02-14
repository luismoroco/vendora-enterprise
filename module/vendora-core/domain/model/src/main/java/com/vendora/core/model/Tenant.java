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
    public static final String TENANT_ID = "tenantId";
    public static final String NAME = "name";
    public static final String DOMAIN = "domain";

    private Long   tenantId;
    private String name;
    private String domain;
}

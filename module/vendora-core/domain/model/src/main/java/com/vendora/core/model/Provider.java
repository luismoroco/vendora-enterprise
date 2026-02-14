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

    public static final String TYPE = "Provider";
    public static final String PROVIDER_ID = "providerId";
    public static final String NAME = "name";
    public static final String RUC = "ruc";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String TENANT_ID = "tenantId";

    private Long   providerId;
    private String name;
    private String ruc;
    private String phone;
    private String email;
    private Long   tenantId;
}

package com.vendora.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    public static final String TYPE = "User";

    public static final String USER_ID = "userId";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TENANT_ID = "tenantId";

    private Long   userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long   tenantId;
}

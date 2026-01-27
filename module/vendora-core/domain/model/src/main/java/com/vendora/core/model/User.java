package com.vendora.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    public static final String TYPE = "User";

    private Long   userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long   tenantId;
}

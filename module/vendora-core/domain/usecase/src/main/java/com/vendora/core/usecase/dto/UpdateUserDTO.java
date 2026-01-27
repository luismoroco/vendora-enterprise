package com.vendora.core.usecase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class UpdateUserDTO {

    private Long   userId;
    private Long   tenantId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}


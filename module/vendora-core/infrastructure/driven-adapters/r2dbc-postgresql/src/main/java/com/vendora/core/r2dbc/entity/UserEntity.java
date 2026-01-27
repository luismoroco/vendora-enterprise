package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id      private Long   userId;
    @NotNull private String firstName;
    @NotNull private String lastName;
    @NotNull private String email;
    @NotNull private String password;
    @NotNull private Long   tenantId;
}


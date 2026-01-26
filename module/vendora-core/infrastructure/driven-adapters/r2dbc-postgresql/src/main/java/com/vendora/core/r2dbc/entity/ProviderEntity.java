package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("provider")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProviderEntity {

    @Id      private Long   providerId;
    @NotNull private String name;
    @NotNull private String ruc;
    @NotNull private String phone;
    @NotNull private String email;
    @NotNull private Long   tenantId;
}


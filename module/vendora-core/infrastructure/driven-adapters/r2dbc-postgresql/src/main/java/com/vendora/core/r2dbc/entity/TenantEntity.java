package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tenant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TenantEntity {

    @Id      private Long   tenantId;
    @NotNull private String name;
    @NotNull private String domain;
}

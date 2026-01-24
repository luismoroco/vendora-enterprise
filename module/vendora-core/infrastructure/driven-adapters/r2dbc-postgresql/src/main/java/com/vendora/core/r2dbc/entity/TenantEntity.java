package com.vendora.core.r2dbc.entity;

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

    @Id
    private Long   tenantId;
    private String name;
    private String domain;
}

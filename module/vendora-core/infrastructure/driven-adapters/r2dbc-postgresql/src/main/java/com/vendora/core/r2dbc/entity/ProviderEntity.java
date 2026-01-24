package com.vendora.core.r2dbc.entity;

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

    @Id
    private Long providerId;
    private String name;
    private String ruc;
    private String phone;
    private String email;
    private Long tenantId;
}


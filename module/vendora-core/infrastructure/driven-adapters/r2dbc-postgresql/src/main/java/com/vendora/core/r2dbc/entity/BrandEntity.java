package com.vendora.core.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("brand")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BrandEntity {

    @Id
    private Long   brandId;
    private String name;
    private String imageUrl;
    private Long   tenantId;
}

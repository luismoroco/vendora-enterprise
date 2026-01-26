package com.vendora.core.r2dbc.entity;

import jakarta.validation.constraints.NotNull;
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

    @Id      private Long   brandId;
    @NotNull private String name;
    @NotNull private String imageUrl;
    @NotNull private Long   tenantId;
}

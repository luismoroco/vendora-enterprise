package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "product_category")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productCategoryId;

  private String name;
  private String imageUrl;
  private Boolean featured;
}

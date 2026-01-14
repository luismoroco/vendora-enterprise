package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Enumerated(EnumType.STRING)
  private ProductStatusType productStatusType;

  private String name;
  private String barCode;
  private Double price;
  private Integer stock;
  private String imageUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @ManyToMany(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.MERGE, CascadeType.REFRESH}
  )
  @JoinTable(
    name = "product_category_product",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "product_category_id")
  )
  private Set<ProductCategory> categories = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id")
  private Brand brand;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

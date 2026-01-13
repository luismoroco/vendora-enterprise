package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;
}

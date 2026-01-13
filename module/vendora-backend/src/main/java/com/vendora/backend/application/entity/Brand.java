package com.vendora.backend.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "brand")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long brandId;

  private String name;
  private String imageUrl;
}

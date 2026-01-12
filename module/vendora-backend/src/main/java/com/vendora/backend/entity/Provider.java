package com.vendora.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "provider")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long providerId;

  private String name;
  private String ruc;
  private String phone;
  private String email;
}

package com.vendora.backend.application.usecase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProviderRequest {
  private Long providerId;
  private String name;
  private String ruc;
  private String phone;
  private String email;
  private String imageUrl;
}

package com.vendora.backend.usecase.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProviderRequest {
  private String name;
  private String ruc;
  private String phone;
  private String email;
}

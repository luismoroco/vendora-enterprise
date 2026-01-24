package com.vendora.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Brand {

  public static final String TYPE = "Brand";

  private Long    brandId;
  private String  name;
  private String  imageUrl;
  private Long    tenantId;
}

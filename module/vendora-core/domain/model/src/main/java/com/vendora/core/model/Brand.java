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

  public static final String BRAND_ID = "brandId";
  public static final String NAME = "name";
  public static final String IMAGE_URL = "imageUrl";
  public static final String TENANT_ID = "tenantId";

  private Long    brandId;
  private String  name;
  private String  imageUrl;
  private Long    tenantId;
}

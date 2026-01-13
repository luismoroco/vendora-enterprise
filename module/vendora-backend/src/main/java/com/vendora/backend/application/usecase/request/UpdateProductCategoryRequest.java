package com.vendora.backend.application.usecase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCategoryRequest {
  private Long productCategoryId;
  private String name;
  private String imageUrl;
  private Boolean featured;
}

package com.vendora.backend.application.usecase.request;

import com.vendora.backend.application.entity.ProductStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
  private Long productId;
  private String name;
  private String barCode;
  private Double price;
  private Integer stock;
  private ProductStatusType productStatusType;
}

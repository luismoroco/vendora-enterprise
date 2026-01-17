package com.vendora.backend.application.usecase.request;

import com.vendora.backend.application.entity.ProductStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
  private String imageUrl;
  private Long brandId;
  private List<Long> productCategoryIds;
  private Long providerId;
  private Double cost;
  private String description;
}

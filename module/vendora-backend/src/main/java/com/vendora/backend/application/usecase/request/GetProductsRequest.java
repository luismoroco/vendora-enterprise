package com.vendora.backend.application.usecase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsRequest {
  private String name;
  private String barCode;
  private List<Long> categoryIds;
  private List<Long> brandIds;
  private List<Long> providerIds;
  private List<Long> productIds;
  private Double minPrice;
  private Double maxPrice;
  private Integer page;
  private Integer size;
}

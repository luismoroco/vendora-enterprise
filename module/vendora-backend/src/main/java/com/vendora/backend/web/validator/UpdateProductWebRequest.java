package com.vendora.backend.web.validator;

import com.vendora.backend.application.entity.ProductStatusType;
import com.vendora.backend.application.usecase.request.UpdateProductRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductWebRequest implements RequestAdapter<UpdateProductRequest> {
  private String name;
  private String barCode;
  private Double price;
  private Integer stock;
  private ProductStatusType productStatusType;
  private String imageUrl;
}

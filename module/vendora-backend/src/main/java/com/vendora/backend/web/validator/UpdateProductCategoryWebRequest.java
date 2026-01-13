package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.UpdateProductCategoryRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCategoryWebRequest implements RequestAdapter<UpdateProductCategoryRequest> {
  private String name;
  private String imageUrl;
  private Boolean featured;
}

package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.CreateProductCategoryRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCategoryWebRequest implements RequestAdapter<CreateProductCategoryRequest> {
  @NotBlank private String name;
  @NotBlank private String imageUrl;
  @NotNull  private Boolean featured;
}

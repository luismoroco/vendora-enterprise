package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.CreateBrandRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandWebRequest implements RequestAdapter<CreateBrandRequest> {
  @NotBlank private String name;
  @NotBlank private String imageUrl;
}

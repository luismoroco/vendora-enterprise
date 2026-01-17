package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.CreateProviderRequest;
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
public class CreateProviderWebRequest implements RequestAdapter<CreateProviderRequest> {
  @NotBlank private String name;
  @NotBlank private String ruc;
  @NotBlank private String phone;
  @NotBlank private String email;
  private String imageUrl;
}

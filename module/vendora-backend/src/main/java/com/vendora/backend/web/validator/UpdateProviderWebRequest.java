package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.UpdateProviderRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProviderWebRequest implements RequestAdapter<UpdateProviderRequest> {
  @Size(min = 2, max = 100)      private String name;
  @Pattern(regexp = "\\d{11}")   private String ruc;
  @Pattern(regexp = "\\d{7,15}") private String phone;
  @Email                         private String email;
}

package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.UpdateBrandRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBrandWebRequest implements RequestAdapter<UpdateBrandRequest> {
  private String name;
  private String imageUrl;
}

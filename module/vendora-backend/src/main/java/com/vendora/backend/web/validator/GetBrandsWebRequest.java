package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.GetBrandsRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBrandsWebRequest implements RequestAdapter<GetBrandsRequest> {
  private List<Long> brandIds;
}

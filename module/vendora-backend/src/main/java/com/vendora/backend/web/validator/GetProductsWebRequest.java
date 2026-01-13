package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.GetProductsRequest;
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
public class GetProductsWebRequest implements RequestAdapter<GetProductsRequest> {
  private List<Long> productIds;
}

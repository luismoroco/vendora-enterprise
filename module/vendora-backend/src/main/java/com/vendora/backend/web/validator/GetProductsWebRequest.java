package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.GetProductsRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetProductsWebRequest implements RequestAdapter<GetProductsRequest> {
  private String name;
  private String barCode;
  private List<Long> categoryIds;
  private List<Long> brandIds;
  private List<Long> providerIds;
  private List<Long> productIds;
  @NotNull private Integer page;
  @NotNull private Integer size;
}

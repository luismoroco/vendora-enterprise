package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.GetProvidersRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProvidersWebRequest implements RequestAdapter<GetProvidersRequest> {
  private String name;
  private List<Long> providerIds;
  @NotNull private Integer page;
  @NotNull private Integer size;
}

package com.vendora.backend.usecase.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProvidersRequest {
  private List<Long> providerIds;
}

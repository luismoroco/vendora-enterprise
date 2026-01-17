package com.vendora.backend.application.usecase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProvidersRequest {
  private String name;
  private List<Long> providerIds;
  private Integer page;
  private Integer size;
}

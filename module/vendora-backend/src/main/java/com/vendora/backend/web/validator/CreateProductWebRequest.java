package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.CreateProductRequest;
import com.vendora.backend.common.web.request.RequestAdapter;
import jakarta.validation.constraints.NotBlank;
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
public class CreateProductWebRequest implements RequestAdapter<CreateProductRequest> {
  @NotBlank  private String name;
  @NotBlank  private String barCode;
  @NotNull   private Double price;
  @NotNull   private Integer stock;
  @NotNull   private Long providerId;
  @NotBlank  private String imageUrl;
  @NotNull   private Long brandId;
  @NotNull   private List<Long> productCategoryIds;
}

package com.vendora.backend.web.validator;

import com.vendora.backend.application.usecase.request.UpdateShoppingCartRequest;
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
public class UpdateShoppingCartWebRequest implements RequestAdapter<UpdateShoppingCartRequest> {
  private List<UpdateShoppingCartRequest.Item> items;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {
    private Long productId;
    private Integer quantity;
  }
}

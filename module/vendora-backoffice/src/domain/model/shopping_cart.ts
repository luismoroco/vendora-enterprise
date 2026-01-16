import type {ShoppingCartItem} from "./shopping_cart_item.ts";

export class ShoppingCart {
  public shoppingCartId: number;
  public items: Array<ShoppingCartItem>;

  public constructor() {
  }
}
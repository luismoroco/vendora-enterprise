import type {ShoppingCartRepository} from "../../domain/repository";
import {HttpCrudRepository} from "../../common/repository/http_crud_repository.ts";
import {ShoppingCart} from "../../domain/model";

export class HttpShoppingCartRepository extends HttpCrudRepository<ShoppingCart, number> implements ShoppingCartRepository {
  public constructor() {
    super('shopping-cart');
  }
}
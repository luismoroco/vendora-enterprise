import type {ProductRepository} from "../../domain/repository";
import {HttpCrudRepository} from "../../common/repository/http_crud_repository.ts";
import {Product} from "../../domain/model";

export class HttpProductRepository extends HttpCrudRepository<Product, number> implements ProductRepository {
  public constructor() {
    super('product');
  }
}
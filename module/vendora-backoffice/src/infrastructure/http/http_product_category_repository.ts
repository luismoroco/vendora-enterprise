import type {ProductCategoryRepository} from "../../domain/repository";
import {HttpCrudRepository} from "../../common/repository/http_crud_repository.ts";
import {ProductCategory} from "../../domain/model";

export class HttpProductCategoryRepository extends HttpCrudRepository<ProductCategory, number> implements ProductCategoryRepository {
  public constructor() {
    super('product-category');
  }
}
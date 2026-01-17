import type {BrandRepository} from "../../domain/repository";
import {HttpCrudRepository} from "../../common/repository/http_crud_repository.ts";
import {Brand} from "../../domain/model";

export class HttpBrandRepository extends HttpCrudRepository<Brand, number> implements BrandRepository {
  public constructor() {
    super('brand');
  }
}
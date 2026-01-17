import type {ProviderRepository} from "../../domain/repository";
import {HttpCrudRepository} from "../../common/repository/http_crud_repository.ts";
import {Provider} from "../../domain/model";

export class HttpProviderRepository extends HttpCrudRepository<Provider, number> implements ProviderRepository {
  public constructor() {
    super('provider');
  }
}
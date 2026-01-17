import type {CrudRepository} from "../../common/repository/crud_repository.ts";
import {Provider} from "../model";

export interface ProviderRepository extends CrudRepository<Provider, number> {}
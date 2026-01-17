import type {CrudRepository} from "../../common/repository/crud_repository.ts";
import {ProductCategory} from "../model";

export interface ProductCategoryRepository extends CrudRepository<ProductCategory, number> {}
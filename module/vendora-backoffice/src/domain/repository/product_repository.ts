import type {CrudRepository} from "../../common/repository/crud_repository.ts";
import {Product} from "../model";

export interface ProductRepository extends CrudRepository<Product, number> {}
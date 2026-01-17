import type {CrudRepository} from "../../common/repository/crud_repository.ts";
import {ShoppingCart} from "../model";

export interface ShoppingCartRepository extends CrudRepository<ShoppingCart, number> {}
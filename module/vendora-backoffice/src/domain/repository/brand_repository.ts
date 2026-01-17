import {Brand} from "../model";
import type {CrudRepository} from "../../common/repository/crud_repository.ts";

export interface BrandRepository extends CrudRepository<Brand, number> {}
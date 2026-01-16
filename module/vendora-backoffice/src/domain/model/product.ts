import type {ProductStatusType} from "./product_status_type.ts";
import {Auditable} from "../../common/model/auditable.ts";

export class Product extends Auditable {
  public productId: number;
  public productStatusType: ProductStatusType;
  public name: string;
  public barCode: string;
  public price: number;
  public stock: number;
  public imageUrl: string;

  public constructor() {
    super();
  }
}
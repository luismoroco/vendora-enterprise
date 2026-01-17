import type {Page} from "../model/page.ts";

export interface CrudRepository<E, I> {
  create(entity: E): Promise<E>;

  update(entity: E): Promise<E>;

  findAll(page: number, size: number): Promise<Page<E>>;

  findById(id: I): Promise<E>;
}
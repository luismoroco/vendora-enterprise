import type {Page} from "../model/page.ts";
import {HttpClient} from "../http";

export class HttpCrudRepository<E, I> {
  protected resource: string;

  protected constructor(resource: string) {
    this.resource = resource;
  }

  public async create(entity: E): Promise<E> {
    return HttpClient.post<E>(`${this.resource}/{id}`, {body: entity});
  }

  public async findAll(page: number, size: number): Promise<Page<E>> {
    return HttpClient.get<Page<E>>(`${this.resource}`);
  }

  public async findById(id: I): Promise<E> {
    return HttpClient.get<E>(`${this.resource}/{id}`);
  }

  public async update(entity: Partial<E>): Promise<E> {
    return HttpClient.put<E>(`${this.resource}/{id}`, {body: entity});
  }
}
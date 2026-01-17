import type {ProductRepository} from "../repository";

export class ProductUseCase {
  private repository: ProductRepository;

  public constructor(repository: ProductRepository) {
    this.repository = repository;
  }
}
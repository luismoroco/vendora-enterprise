import type {ProductCategoryRepository} from "../repository";

export class ProductCategoryUseCase {
  private repository: ProductCategoryRepository;

  public constructor(repository: ProductCategoryRepository) {
    this.repository = repository;
  }
}
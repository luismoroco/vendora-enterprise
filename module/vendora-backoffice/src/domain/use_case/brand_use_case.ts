import type {BrandRepository} from "../repository";

export class BrandUseCase {
  private repository: BrandRepository;

  public constructor(repository: BrandRepository) {
    this.repository = repository;
  }
}
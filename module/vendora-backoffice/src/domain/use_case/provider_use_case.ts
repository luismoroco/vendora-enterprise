import type {ProviderRepository} from "../repository";

export class ProviderUseCase {
  private repository: ProviderRepository;

  public constructor(repository: ProviderRepository) {
    this.repository = repository;
  }
}
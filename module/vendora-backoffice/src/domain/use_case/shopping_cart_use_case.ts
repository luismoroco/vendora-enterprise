import type {ShoppingCartRepository} from "../repository";

export class ShoppingCartUseCase {
  private repository: ShoppingCartRepository;

  public constructor(repository: ShoppingCartRepository) {
    this.repository = repository;
  }
}
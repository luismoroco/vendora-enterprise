// API Types
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface Product {
  productId: number;
  name: string;
  barCode: string;
  price: number;
  stock: number;
  imageUrl: string;
  productStatusType: 'ENABLED' | 'DISABLED';
  provider: Provider;
  brand: Brand;
  categories: ProductCategory[];
  createdAt: string;
  updatedAt: string;
}

export interface Brand {
  brandId: number;
  name: string;
  imageUrl: string;
}

export interface ProductCategory {
  productCategoryId: number;
  name: string;
  imageUrl: string;
  featured: boolean;
}

export interface Provider {
  providerId: number;
  name: string;
  ruc: string;
  phone: string;
  email: string;
}

// Request Types
export interface GetProductsParams {
  name?: string;
  barCode?: string;
  categoryIds?: number[];
  brandIds?: number[];
  providerIds?: number[];
  productIds?: number[];
  page: number;
  size: number;
}

export interface GetBrandsParams {
  name?: string;
  brandIds?: number[];
  page: number;
  size: number;
}

export interface GetProductCategoriesParams {
  name?: string;
  productCategoryIds?: number[];
  page: number;
  size: number;
}

export interface GetProvidersParams {
  name?: string;
  providerIds?: number[];
  page: number;
  size: number;
}

export interface CreateProductRequest {
  name: string;
  barCode: string;
  price: number;
  stock: number;
  imageUrl: string;
  providerId: number;
  brandId: number;
  productCategoryIds: number[];
}

export interface UpdateProductRequest {
  name?: string;
  barCode?: string;
  price?: number;
  stock?: number;
  imageUrl?: string;
  productStatusType?: 'ENABLED' | 'DISABLED';
  productCategoryIds?: number[];
}

export interface CreateBrandRequest {
  name: string;
  imageUrl: string;
}

export interface UpdateBrandRequest {
  name?: string;
  imageUrl?: string;
}

export interface CreateProductCategoryRequest {
  name: string;
  imageUrl: string;
  featured: boolean;
}

export interface UpdateProductCategoryRequest {
  name?: string;
  imageUrl?: string;
  featured?: boolean;
}

export interface CreateProviderRequest {
  name: string;
  ruc: string;
  phone: string;
  email: string;
}

export interface UpdateProviderRequest {
  name?: string;
  ruc?: string;
  phone?: string;
  email?: string;
}

// Cart Types
export interface CartItem extends Product {
  quantity: number;
}


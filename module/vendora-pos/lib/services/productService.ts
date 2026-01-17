import apiClient from '../api';
import type { Product, Page, GetProductsParams, CreateProductRequest, UpdateProductRequest } from '../types';

export const productService = {
  getProducts: async (params: GetProductsParams): Promise<Page<Product>> => {
    const response = await apiClient.get<Page<Product>>('/products', { params });
    return response.data;
  },

  getProductById: async (productId: number): Promise<Product> => {
    const response = await apiClient.get<Product>(`/products/${productId}`);
    return response.data;
  },

  createProduct: async (data: CreateProductRequest): Promise<Product> => {
    const response = await apiClient.post<Product>('/products', data);
    return response.data;
  },

  updateProduct: async (productId: number, data: UpdateProductRequest): Promise<Product> => {
    const response = await apiClient.put<Product>(`/products/${productId}`, data);
    return response.data;
  },
};


import apiClient from '../api';
import type { ProductCategory, Page, GetProductCategoriesParams, CreateProductCategoryRequest, UpdateProductCategoryRequest } from '../types';

export const categoryService = {
  getCategories: async (params: GetProductCategoriesParams): Promise<Page<ProductCategory>> => {
    const response = await apiClient.get<Page<ProductCategory>>('/product-categories', { params });
    return response.data;
  },

  getCategoryById: async (categoryId: number): Promise<ProductCategory> => {
    const response = await apiClient.get<ProductCategory>(`/product-categories/${categoryId}`);
    return response.data;
  },

  createCategory: async (data: CreateProductCategoryRequest): Promise<ProductCategory> => {
    const response = await apiClient.post<ProductCategory>('/product-categories', data);
    return response.data;
  },

  updateCategory: async (categoryId: number, data: UpdateProductCategoryRequest): Promise<ProductCategory> => {
    const response = await apiClient.put<ProductCategory>(`/product-categories/${categoryId}`, data);
    return response.data;
  },
};


import apiClient from '../api';
import type { Brand, Page, GetBrandsParams, CreateBrandRequest, UpdateBrandRequest } from '../types';

export const brandService = {
  getBrands: async (params: GetBrandsParams): Promise<Page<Brand>> => {
    const response = await apiClient.get<Page<Brand>>('/brands', { params });
    return response.data;
  },

  getBrandById: async (brandId: number): Promise<Brand> => {
    const response = await apiClient.get<Brand>(`/brands/${brandId}`);
    return response.data;
  },

  createBrand: async (data: CreateBrandRequest): Promise<Brand> => {
    const response = await apiClient.post<Brand>('/brands', data);
    return response.data;
  },

  updateBrand: async (brandId: number, data: UpdateBrandRequest): Promise<Brand> => {
    const response = await apiClient.put<Brand>(`/brands/${brandId}`, data);
    return response.data;
  },
};


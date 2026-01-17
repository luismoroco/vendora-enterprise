import apiClient from '../api';
import type { Provider, Page, GetProvidersParams, CreateProviderRequest, UpdateProviderRequest } from '../types';

export const providerService = {
  getProviders: async (params: GetProvidersParams): Promise<Page<Provider>> => {
    const response = await apiClient.get<Page<Provider>>('/providers', { params });
    return response.data;
  },

  getProviderById: async (providerId: number): Promise<Provider> => {
    const response = await apiClient.get<Provider>(`/providers/${providerId}`);
    return response.data;
  },

  createProvider: async (data: CreateProviderRequest): Promise<Provider> => {
    const response = await apiClient.post<Provider>('/providers', data);
    return response.data;
  },

  updateProvider: async (providerId: number, data: UpdateProviderRequest): Promise<Provider> => {
    const response = await apiClient.put<Provider>(`/providers/${providerId}`, data);
    return response.data;
  },
};


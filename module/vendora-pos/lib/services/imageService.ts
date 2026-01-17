import apiClient from '../api';

export const imageService = {
  uploadImage: async (file: File): Promise<{ url: string; filename: string }> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post<{ url: string; filename: string }>(
      '/images/upload',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );

    return response.data;
  },

  deleteImage: async (filename: string): Promise<void> => {
    await apiClient.delete(`/images/${filename}`);
  },
};


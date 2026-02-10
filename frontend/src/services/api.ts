import axios, { AxiosInstance } from "axios";
import {
  RawMaterial,
  RawMaterialFormData,
  Product,
  ProductFormData,
  AddMaterialToProductData,
  ProductionSuggestionsResponse,
} from "../types";

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Raw Materials API
export const rawMaterialsApi = {
  getAll: () => api.get<RawMaterial[]>("/rawMaterials"),
  getById: (id: number) => api.get<RawMaterial>(`/rawMaterials/${id}`),
  create: (data: RawMaterialFormData) =>
    api.post<RawMaterial>("/rawMaterials", data),
  update: (id: number, data: RawMaterialFormData) =>
    api.put<RawMaterial>(`/rawMaterials/${id}`, data),
  delete: (id: number) => api.delete(`/rawMaterials/${id}`),
};

// Products API
export const productsApi = {
  getAll: () => api.get<Product[]>("/products"),
  getById: (id: number) => api.get<Product>(`/products/${id}`),
  create: (data: ProductFormData) => api.post<Product>("/products", data),
  update: (id: number, data: ProductFormData) =>
    api.put<Product>(`/products/${id}`, data),
  delete: (id: number) => api.delete(`/products/${id}`),
  addMaterial: (
    productId: number,
    data: Omit<AddMaterialToProductData, "productId">,
  ) => api.post(`/products/${productId}/materials`, data),
  removeMaterial: (productId: number, materialId: number) =>
    api.delete(`/products/${productId}/materials/${materialId}`),
  getProducible: () => api.get<Product[]>("/products/producible"),
  getProductionSuggestions: () =>
    api.get<ProductionSuggestionsResponse>("/products/production-suggestions"),
};

export default api;

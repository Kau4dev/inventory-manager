// Raw Material Types
export interface RawMaterial {
  id: number;
  name: string;
  stockQuantity: number;
}

export interface RawMaterialFormData {
  name: string;
  stockQuantity: number;
}

// Product Types
export interface ProductMaterial {
  id: number;
  materialId: number;
  materialName: string;
  requiredQuantity: number;
  unitPrice: number;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  materials: ProductMaterial[];
  producible: boolean;
  maxProducible?: number;
}

export interface ProductFormData {
  name: string;
  price: number;
}

export interface AddMaterialToProductData {
  productId: number;
  materialId: number;
  requiredQuantity: number;
}

export interface RemoveMaterialFromProductData {
  productId: number;
  materialId: number;
}

// Production Suggestions Types
export interface ProductionSuggestion {
  id: number;
  name: string;
  price: number;
  maxQuantity: number;
  totalValue: number;
}

export interface ProductionSuggestionsResponse {
  suggestions: ProductionSuggestion[];
  totalValue: number;
}

// API Response Types
export interface ApiError {
  message: string;
  status?: number;
}

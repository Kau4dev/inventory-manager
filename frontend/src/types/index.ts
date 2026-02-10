// Raw Material Types
export interface RawMaterial {
  id: number;
  name: string;
  quantity: number;
  unitPrice: number;
  minimumStock: number;
}

export interface RawMaterialFormData {
  name: string;
  quantity: number;
  unitPrice: number;
  minimumStock: number;
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
  description: string;
  price: number;
  materials: ProductMaterial[];
  producible: boolean;
  maxProducible?: number;
}

export interface ProductFormData {
  name: string;
  description: string;
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
  id?: number;
  productId: number;
  productName: string;
  maxQuantity: number;
  profitPerUnit: number;
  totalProfit: number;
  suggestedQuantity: number;
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

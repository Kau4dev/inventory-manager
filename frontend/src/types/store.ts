import { RawMaterial, Product, ProductionSuggestionsResponse } from "./index";

// Raw Materials State
export interface RawMaterialsState {
  items: RawMaterial[];
  loading: boolean;
  error: string | null;
}

// Products State
export interface ProductsState {
  items: Product[];
  productionSuggestions: ProductionSuggestionsResponse;
  loading: boolean;
  error: string | null;
}

// UI State
export interface UIState {
  modalOpen: boolean;
  modalType: string | null;
  sidebarOpen: boolean;
  modalData: any;
  notification: { type: string; message: string } | null;
}

// Root State
export interface RootState {
  rawMaterials: RawMaterialsState;
  products: ProductsState;
  ui: UIState;
}

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { productsApi } from "../../services/api";
import {
  Product,
  ProductFormData,
  ProductionSuggestionsResponse,
  AddMaterialToProductData,
  RemoveMaterialFromProductData,
} from "../../types";
import { ProductsState } from "../../types/store";

interface UpdateProductPayload {
  id: number;
  data: ProductFormData;
}

export const fetchProducts = createAsyncThunk<
  Product[],
  void,
  { rejectValue: string }
>("products/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await productsApi.getAll();
    return response.data;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error fetching products",
    );
  }
});

export const createProduct = createAsyncThunk<
  Product,
  ProductFormData,
  { rejectValue: string }
>("products/create", async (data, { rejectWithValue }) => {
  try {
    const response = await productsApi.create(data);
    return response.data;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error creating product",
    );
  }
});

export const updateProduct = createAsyncThunk<
  { id: number; data: Product },
  UpdateProductPayload,
  { rejectValue: string }
>("products/update", async ({ id, data }, { rejectWithValue }) => {
  try {
    const response = await productsApi.update(id, data);
    return { id, data: response.data };
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error updating product",
    );
  }
});

export const deleteProduct = createAsyncThunk<
  number,
  number,
  { rejectValue: string }
>("products/delete", async (id, { rejectWithValue }) => {
  try {
    await productsApi.delete(id);
    return id;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error deleting product",
    );
  }
});

export const addMaterialToProduct = createAsyncThunk<
  AddMaterialToProductData,
  AddMaterialToProductData,
  { rejectValue: string }
>(
  "products/addMaterial",
  async ({ productId, materialId, requiredQuantity }, { rejectWithValue }) => {
    try {
      await productsApi.addMaterial(productId, {
        materialId,
        requiredQuantity,
      });
      return { productId, materialId, requiredQuantity };
    } catch (error: any) {
      return rejectWithValue(
        error.response?.data?.message || "Error adding material to product",
      );
    }
  },
);

export const removeMaterialFromProduct = createAsyncThunk<
  RemoveMaterialFromProductData,
  RemoveMaterialFromProductData,
  { rejectValue: string }
>(
  "products/removeMaterial",
  async ({ productId, materialId }, { rejectWithValue }) => {
    try {
      await productsApi.removeMaterial(productId, materialId);
      return { productId, materialId };
    } catch (error: any) {
      return rejectWithValue(
        error.response?.data?.message || "Error removing material from product",
      );
    }
  },
);

export const fetchProductionSuggestions = createAsyncThunk<
  ProductionSuggestionsResponse,
  void,
  { rejectValue: string }
>("products/fetchProductionSuggestions", async (_, { rejectWithValue }) => {
  try {
    const response = await productsApi.getProductionSuggestions();
    return response.data;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error fetching production suggestions",
    );
  }
});

const initialState: ProductsState = {
  items: [],
  productionSuggestions: {
    suggestions: [],
    totalValue: 0,
  },
  loading: false,
  error: null,
};

const productsSlice = createSlice({
  name: "products",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch
      .addCase(fetchProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Create
      .addCase(createProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.loading = false;
        state.items.push(action.payload);
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Update
      .addCase(updateProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateProduct.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.items.findIndex(
          (item: Product) => item.id === action.payload.id,
        );
        if (index !== -1) {
          state.items[index] = {
            ...state.items[index],
            ...action.payload.data,
          };
        }
      })
      .addCase(updateProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Delete
      .addCase(deleteProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteProduct.fulfilled, (state, action) => {
        state.loading = false;
        state.items = state.items.filter(
          (item: Product) => item.id !== action.payload,
        );
      })
      .addCase(deleteProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Add Material
      .addCase(addMaterialToProduct.rejected, (state, action) => {
        state.error = action.payload || "Unknown error";
      })
      // Remove Material
      .addCase(removeMaterialFromProduct.rejected, (state, action) => {
        state.error = action.payload || "Unknown error";
      })
      // Production Suggestions
      .addCase(fetchProductionSuggestions.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProductionSuggestions.fulfilled, (state, action) => {
        state.loading = false;
        state.productionSuggestions = action.payload;
      })
      .addCase(fetchProductionSuggestions.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      });
  },
});

export const { clearError } = productsSlice.actions;
export default productsSlice.reducer;

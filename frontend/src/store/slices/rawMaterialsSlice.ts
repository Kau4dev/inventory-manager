import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { rawMaterialsApi } from "../../services/api";
import { RawMaterial, RawMaterialFormData } from "../../types";
import { RawMaterialsState } from "../../types/store";

interface UpdateRawMaterialPayload {
  id: number;
  data: RawMaterialFormData;
}

export const fetchRawMaterials = createAsyncThunk<
  RawMaterial[],
  void,
  { rejectValue: string }
>("rawMaterials/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await rawMaterialsApi.getAll();
    return response.data;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error fetching raw materials",
    );
  }
});

export const createRawMaterial = createAsyncThunk<
  RawMaterial,
  RawMaterialFormData,
  { rejectValue: string }
>("rawMaterials/create", async (data, { rejectWithValue }) => {
  try {
    const response = await rawMaterialsApi.create(data);
    return response.data;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error creating raw material",
    );
  }
});

export const updateRawMaterial = createAsyncThunk<
  { id: number; data: RawMaterial },
  UpdateRawMaterialPayload,
  { rejectValue: string }
>("rawMaterials/update", async ({ id, data }, { rejectWithValue }) => {
  try {
    const response = await rawMaterialsApi.update(id, data);
    return { id, data: response.data };
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error updating raw material",
    );
  }
});

export const deleteRawMaterial = createAsyncThunk<
  number,
  number,
  { rejectValue: string }
>("rawMaterials/delete", async (id, { rejectWithValue }) => {
  try {
    await rawMaterialsApi.delete(id);
    return id;
  } catch (error: any) {
    return rejectWithValue(
      error.response?.data?.message || "Error deleting raw material",
    );
  }
});

const initialState: RawMaterialsState = {
  items: [],
  loading: false,
  error: null,
};

const rawMaterialsSlice = createSlice({
  name: "rawMaterials",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch
      .addCase(fetchRawMaterials.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRawMaterials.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchRawMaterials.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Create
      .addCase(createRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createRawMaterial.fulfilled, (state, action) => {
        state.loading = false;
        state.items.push(action.payload);
      })
      .addCase(createRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Update
      .addCase(updateRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateRawMaterial.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.items.findIndex(
          (item: RawMaterial) => item.id === action.payload.id,
        );
        if (index !== -1) {
          state.items[index] = {
            ...state.items[index],
            ...action.payload.data,
          };
        }
      })
      .addCase(updateRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      })
      // Delete
      .addCase(deleteRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteRawMaterial.fulfilled, (state, action) => {
        state.loading = false;
        state.items = state.items.filter(
          (item: RawMaterial) => item.id !== action.payload,
        );
      })
      .addCase(deleteRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "Unknown error";
      });
  },
});

export const { clearError } = rawMaterialsSlice.actions;
export default rawMaterialsSlice.reducer;

import { configureStore } from "@reduxjs/toolkit";
import rawMaterialsReducer from "./slices/rawMaterialsSlice";
import productsReducer from "./slices/productsSlice";
import uiReducer from "./slices/uiSlice";

export const store = configureStore({
  reducer: {
    rawMaterials: rawMaterialsReducer,
    products: productsReducer,
    ui: uiReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;

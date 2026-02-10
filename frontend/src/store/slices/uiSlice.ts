import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface Notification {
  type: string;
  message: string;
}

interface ModalPayload {
  type: string;
  data?: any;
}

interface UIState {
  sidebarOpen: boolean;
  modalOpen: boolean;
  modalType: string | null;
  modalData: any;
  notification: Notification | null;
}

const initialState: UIState = {
  sidebarOpen: false,
  modalOpen: false,
  modalType: null,
  modalData: null,
  notification: null,
};

const uiSlice = createSlice({
  name: "ui",
  initialState,
  reducers: {
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    openModal: (state, action: PayloadAction<ModalPayload>) => {
      state.modalOpen = true;
      state.modalType = action.payload.type;
      state.modalData = action.payload.data || null;
    },
    closeModal: (state) => {
      state.modalOpen = false;
      state.modalType = null;
      state.modalData = null;
    },
    showNotification: (state, action: PayloadAction<Notification>) => {
      state.notification = {
        type: action.payload.type,
        message: action.payload.message,
      };
    },
    clearNotification: (state) => {
      state.notification = null;
    },
  },
});

export const {
  toggleSidebar,
  openModal,
  closeModal,
  showNotification,
  clearNotification,
} = uiSlice.actions;

export default uiSlice.reducer;

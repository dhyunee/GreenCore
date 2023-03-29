import { createSlice } from '@reduxjs/toolkit';
import { AlertType } from './alertType';
import * as alertAPI from './alertAPI';

interface AlertState {
  isLoading: boolean;
  alertList: Array<AlertType>;
  selectedAlertList: Array<AlertType>;
  size: number;
  page: number;
  lastPage: any;
}

const initialState: AlertState = {
  isLoading: true,
  alertList: [],
  selectedAlertList: [],
  size: 10,
  page: 0,
  lastPage: null,
};

const alertSlice = createSlice({
  name: 'alret',
  initialState,

  reducers: {},

  extraReducers(builder) {
    builder
      .addCase(alertAPI.getAlertList.pending, (state) => {
        state.isLoading = true;
        state.alertList = [];
      })
      .addCase(alertAPI.getAlertList.fulfilled, (state, action) => {
        state.isLoading = false;
        state.alertList = action.payload.alertList;
        state.lastPage = action.payload.lastPage;
      });
    // .addCase(alertAPI.getAlertList.pending, (state) => {})
    // .addCase(alertAPI.getAlertList.fulfilled, (state, action) => {
    //   state.isLoading = false;
    //   if (action.payload.length === 0) {
    //     state.isStoped = true;
    //   }
    //   state.page = 1;
    //   state.alertList = action.payload;
    // })
    // .addCase(alertAPI.getAlertListMore.pending, (state) => {})
    // .addCase(alertAPI.getAlertListMore.fulfilled, (state, action) => {
    //   if (action.payload.length === 0) {
    //     state.isStoped = true;
    //   }
    //   state.page = state.page + 1;
    //   state.alertList = [...state.alertList, ...action.payload];
    // });
  },
});

export default alertSlice.reducer;

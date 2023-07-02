import storage from "redux-persist/lib/storage"
import {combineReducers, configureStore, getDefaultMiddleware} from "@reduxjs/toolkit";
import usernameSlice from "@/lib/redux/slices/usernameSlice"
import {persistReducer, persistStore} from "redux-persist";

const persistConfig = {
    key: 'app',
    storage
}

const reducers = combineReducers({
    username: usernameSlice
})

const persistedReducer = persistReducer(persistConfig, reducers)

export default function makeStore() {
    let store = configureStore({
        reducer: persistedReducer,
        middleware: getDefaultMiddleware({
            serializableCheck: false
        })
    })
    let persistor = persistStore(store)
    return {store, persistor}
}
import {Provider} from "react-redux";
import makeStore from "@/lib/redux/store";
import {PersistGate} from "redux-persist/integration/react";

const {store, persistor} = makeStore()

export default function App({Component, pageProps}) {
    return (
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                {
                    Component.pageLayout ?
                        <Component.pageLayout>
                            <Component {...pageProps}/>
                        </Component.pageLayout> :
                        <Component {...pageProps}/>
                }
            </PersistGate>
        </Provider>
    )
}
export default function App({Component, pageProps}) {
    return (
        <>
            {
                Component.pageLayout ?
                    <Component.pageLayout>
                        <Component {...pageProps}/>
                    </Component.pageLayout> :
                    <Component {...pageProps}/>
            }
        </>
    )
}
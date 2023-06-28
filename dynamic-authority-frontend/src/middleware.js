import {NextResponse} from "next/server";

export function middleware(request) {
    const cookies = request.cookies
    if (cookies.has("token")) {
        const requestHeaders = new Headers(request.headers);
        requestHeaders.set("Authorization", "Bearer " + cookies.get("token").value)
        return NextResponse.next({
            request: {
                headers: requestHeaders
            }
        })
    }
    return NextResponse.next({
        request: request
    })
}

export const config = {
    matcher: "/api/:path*"
}
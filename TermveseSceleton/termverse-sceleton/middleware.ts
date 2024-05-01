import {NextRequest, NextResponse} from "next/server";
import axios from "axios";
import {RequestCookies} from "next/dist/compiled/@edge-runtime/cookies";
import {NextURL} from "next/dist/server/web/next-url";

const notProtected = ['/', '/signin', "/signup"];

export function middleware(req: NextRequest) {
    const { nextUrl: { pathname }, cookies } = req;
    console.log("PROTECTED")
    if (notProtected.includes(pathname)) {
        return NextResponse.next();
    }
    const url = req.nextUrl.clone();
    url.pathname = '/signin';
    if (cookies.has("termverse_access_token")) {
        const cookie = cookies.get("termverse_access_token");
        console.log("HAS TOKEN")
        if (cookie)
            return validateToken(cookie.value).then(result => {
            if (result) {
                return NextResponse.next();
            } else {
                if (!cookies.has("termverse_refresh_token")) {
                    return NextResponse.redirect(url);
                }
                const termverseRefreshToken = cookies.get("termverse_refresh_token");
                if (!termverseRefreshToken?.value) {
                    return NextResponse.redirect(url);
                }
                return refresh(termverseRefreshToken?.value, cookies, req, url);
            }
        });
    }
    return NextResponse.redirect(url, {
        status: 303
    });
}
async function refresh(token: string, cookies: RequestCookies, req: NextRequest, url: NextURL) {
    try {
        const response = await fetch('http://localhost:8180/realms/termverse/protocol/openid-connect/token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                client_id: process.env.KEYCLOAK_CLIENT_ID,
                client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
                grant_type: "refresh_token",
                refresh_token: token
            }).toString()
        });
        const jsonData = await response.json();
        const accessToken = jsonData.access_token;
        if (!accessToken) {
            return NextResponse.redirect(url)
        }
        const nextResponse = NextResponse.next();
        nextResponse.headers.set("Set-Cookie", `termverse_access_token=${accessToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}\``)
        return nextResponse;
    } catch (Error) {
        return  NextResponse.redirect(new URL('/signin', req.url), {
            status: 303
        });
    }
}

async function validateToken(token: string) {
    try {
        const response = await fetch('http://localhost:8080/validate', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });
        const jsonData = await response.json();
        const data = jsonData.status;
        return data === "token-valid";
    } catch (error) {
        console.error('Error validating token:', error);
        return false;
    }
}
export const config = {
    matcher: [
        '/profile/:path*',
        '/sets/:path*',
        '/users/:path*'
    ]
}
import {NextRequest, NextResponse} from "next/server";
import axios from "axios";
import {RequestCookies} from "next/dist/compiled/@edge-runtime/cookies";

const notProtected = ['/', '/signin', "/signup"];

export function middleware(req: NextRequest) {
    const { nextUrl: { pathname }, cookies } = req;

    if (notProtected.includes(pathname)) {
        return NextResponse.next();
    }


    if (cookies.has("termverse_access_token")) {
        const cookie = cookies.get("termverse_access_token");

        if (cookie)
            return validateToken(cookie.value).then(result => {
            if (result) {
                return NextResponse.next();
            } else {
                if (!cookies.has("termverse_refresh_token")) {
                    return NextResponse.redirect(new URL('/signin', req.url));
                }
                return refresh(cookies.get("termverse_refresh_token")?.value!, cookies, req);
            }
        });
    }
    return NextResponse.redirect(new URL('/signin', req.url));
}
async function refresh(token: string, cookies: RequestCookies, req: NextRequest) {
    try {
        const response = await axios.post('http://localhost:8180/realms/termverse/protocol/openid-connect/token',
            {
                client_id: process.env.KEYCLOAK_CLIENT_ID,
                client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
                grant_type: "refresh_token",
                refresh_token: cookies.get("termverse_refresh_token")
            });
        const accessToken = response.data.access_token;
        const nextResponse = NextResponse.next();
        nextResponse.cookies.set("Set-Cookie", `termverse_refresh_token=${accessToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}\``)
        return nextResponse;
    } catch (Error) {
        return  NextResponse.redirect(new URL('/signin', req.url));
    }
}

async function validateToken(token: string) {
    try {
        const response = await axios.get('http://localhost:8080/validate', {
            headers: { 'Content-Type': 'application/json' },
        });
        const data = await response.data.status;
        console.log(data);
        return data === "token-valid";
    } catch (error) {
        console.error('Error validating token:', error);
        return false;
    }
}
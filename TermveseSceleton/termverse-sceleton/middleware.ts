import {NextRequest, NextResponse} from "next/server";
import axios from "axios";
import {RequestCookies} from "next/dist/compiled/@edge-runtime/cookies";
import {NextURL} from "next/dist/server/web/next-url";
import jwt, { JwtPayload } from 'jsonwebtoken'
const notProtected = ['/', '/signin', "/signup"];
import { getCookie } from 'cookies-next';
export function middleware(req: NextRequest) {
    const { nextUrl: { pathname }, cookies } = req;
    console.log("PROTECTED")
    if (notProtected.includes(pathname)) {
        return NextResponse.next();
    }
    const url = req.nextUrl.clone();
    url.pathname = '/signin';
    if (kcPublicKey === null) {
        kcPublicKey = getKeycloakPublicKey("localhost:8180/termverse-app")
    }
    if (cookies.has("termverse_access_token")) {
        const cookie = getCookie("termverse_access_token")!;
        console.log("HAS TOKEN")
        if (cookie) {
            const isAccessTokenValid = validateToken(cookie, kcPublicKey)
            if (isAccessTokenValid) {
                return NextResponse.next();
            } else {
                if (!cookies.has("termverse_refresh_token")) {
                    return NextResponse.redirect(url);
                }
                const termverseRefreshToken = getCookie("termverse_refresh_token")!;
                const isRefreshTokenValid = validateToken(termverseRefreshToken, kcPublicKey);
                if (!isRefreshTokenValid) {
                    return NextResponse.redirect(url);
                }
                return refresh(termverseRefreshToken, cookies, req, url);
            }
        }
    }
    return NextResponse.redirect(url);
}
async function refresh(token: string, cookies: RequestCookies, req: NextRequest, url: NextURL) {
    try {
        const response = await fetch('http://localhost:8180/realms/termverse/protocol/openid-connect/token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams(
                {
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
        return NextResponse.redirect(new URL('/signin', req.url));
    }
}

let kcPublicKey = null;
interface JWK {
    kid: string;
    kty: string;
    alg: string;
    use: string;
    n: string;
    e: string;
    x5c: string[];
    x5t: string;
    'x5t#256': string;
}

export interface JWKS {
    keys: JWK[];
}
async function getKeycloakPublicKey(realmUrl: string): Promise<string> {
    let key = "";
    try {
        const response = await axios.get(`${realmUrl}/protocol/openid-connect/certs`);
        const jwks: JWKS = response.data;
        key = jwks.keys.find((k: any) => k.use === 'sig').x5c[0];
    } catch (error) {
        throw new Error('Failed to fetch public key from Keycloak');
    }
    if (key === "") {
        throw new Error('No signing key found in JWKS');
    }
    return  `-----BEGIN CERTIFICATE-----
    ${key}
    -----END CERTIFICATE-----`;

}
function validateToken(token: string, key: string) {
    try {
        const decoded = jwt.verify(token, key, {algorithms: ['RS256']}) as JwtPayload;
        const currentTime = Math.floor(Date.now() / 1000);
        if (decoded.exp && decoded.exp < currentTime) {
            return false;
        }
        return true;
    } catch (error) {
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
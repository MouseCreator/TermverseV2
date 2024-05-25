import {NextRequest, NextResponse} from "next/server";
import {RequestCookies} from "next/dist/compiled/@edge-runtime/cookies";
import {NextURL} from "next/dist/server/web/next-url";
import {importJWK, JWTPayload, jwtVerify, KeyLike} from 'jose';
const notProtected = ['/', '/signin', "/signup"];
export async function middleware(req: NextRequest) {
    const { nextUrl: { pathname }, cookies } = req;
    console.log("PROTECTED")
    if (notProtected.includes(pathname)) {
        return NextResponse.next();
    }
    const url = req.nextUrl.clone();
    url.pathname = '/signin';
    if (cookies.has("termverse_access_token")) {
        const cookie = cookies.get('termverse_access_token')!;
        console.log("HAS ACCESS TOKEN");
        if (cookie) {
            const isAccessTokenValid = validateToken(cookie.value, kcPublicKey)
            if (isAccessTokenValid) {
                console.log("ACCESS TOKEN IS VALID");
                return NextResponse.next();
            } else {
                console.log("ACCESS TOKEN IS INVALID");
                if (!cookies.has("termverse_refresh_token")) {
                    console.log("NO REFRESH TOKEN");
                    return NextResponse.redirect(url);
                }
                console.log("HAS REFRESH TOKEN");
                const termverseRefreshToken = cookies.get('termverse_refresh_token')!;
                const isRefreshTokenValid = validateToken(termverseRefreshToken.value, kcPublicKey);
                if (!isRefreshTokenValid) {
                    console.log("REFRESH TOKEN IS INVALID");
                    return NextResponse.redirect(url);
                }
                console.log("REFRESH TOKEN IS VALID. REFRESHING...")
                return refresh(termverseRefreshToken.value, cookies, req, url);
            }
        } else {
            console.log("NO COOKIE!");
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

const jwk = await getKeycloakPublicKey();
const kcPublicKey = await importJWK(jwk);
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
async function getKeycloakPublicKey(): Promise<JWK> {
    const url = `http://localhost:8180/realms/termverse/protocol/openid-connect/certs`;
    try {
        const response = await fetch(url);
        const jwks: JWKS = await response.json();
        const key = jwks.keys.find((k: any) => k.use === 'sig');
        console.log(`Successfully got KEY for middleware.`)
        return key;
    } catch (error) {
        throw new Error(`Failed to fetch public key from Keycloak: ${url}. Details: ${error}`);
    }

}
async function validateToken(token: string, key: KeyLike | Uint8Array ) {
    try {
        console.log(`VALIDATING TOKEN...`);
        const decoded: JWTPayload = await jwtVerify(token, key);
        const currentTime = Math.floor(Date.now() / 1000);
        if (decoded.exp && decoded.exp < currentTime) {
            return false;
        }
        return true;
    } catch (error) {
        console.log(`VALIDATING ERROR: ${error}`);
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
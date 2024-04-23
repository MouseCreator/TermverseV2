import { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import {useRouter} from "next/router";

export function useActive() {
    const [isActive, setIsActive] = useState('loading');

    useEffect(() => {
        async function checkToken() {
            const accessToken = Cookies.get('termverse_access_token');
            if (!accessToken) {
                setIsActive('inactive');
                console.log("No access token...")
                return;
            }
            console.log("Has access token...")
            try {
                const validationResponse = await axios.post('/api/validate_token', { token: accessToken });
                const validationResult = validationResponse.data.result;

                if (validationResult === 'expired') {
                    const refreshToken = Cookies.get('termverse_refresh_token');
                    const refreshResponse = await axios.post('/api/refresh_token', { token: refreshToken });
                    const newAccessToken = refreshResponse.data.accessToken;
                    const newRefreshToken = refreshResponse.data.refresh_token;

                    if (newAccessToken) {
                        Cookies.set('termverse_access_token', newAccessToken, { expires: 1, secure:  process.env.NODE_ENV !== 'development', sameSite: 'Strict' });
                        Cookies.set('termverse_refresh_token', newRefreshToken, { expires: 1, secure:  process.env.NODE_ENV !== 'development', sameSite: 'Strict' });
                        setIsActive('active');
                    } else {
                        setIsActive('inactive');
                    }
                } else if (validationResult === 'valid') {
                    setIsActive('active');
                } else {
                    setIsActive('inactive');
                }
            } catch (error) {
                console.error('Error checking token:', error);
                setIsActive('inactive');
            }
        }

        checkToken();
    }, []);

    return isActive;
}
export function useActiveOrRedirect() {
    const active = useActive();
    if (!active) {
        console.log('Not active')
        return;
    }
}
import { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { refreshToken } = req.body;

    if (!refreshToken) {
        return res.status(400).json({ error: 'Refresh token is required' });
    }

    try {
        const response = await axios.post(
            'http://localhost:8180/realms/termverse/protocol/openid-connect/token',
            {grant_type: "refresh_token",
            refresh_token: refreshToken,
            client_id: process.env.KEYCLOAK_CLIENT_ID,
            client_secret: process.env.KEYCLOAK_CLIENT_SECRET},
            {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

        res.setHeader('Set-Cookie', [
            `accessToken=${response.data.access_token}; Path=/; HttpOnly; Secure; SameSite=Strict`,
            `refreshToken=${response.data.refresh_token}; Path=/; HttpOnly; Secure; SameSite=Strict`
        ]);

        res.status(200).json({ accessToken: response.data.access_token, refreshToken: response.data.refresh_token });
    } catch (error) {
        console.error('Failed to refresh token:', error);
        res.status(500).json({ error: 'Failed to refresh token' });
    }
}
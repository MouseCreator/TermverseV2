import type { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'POST') {
        const { login, password } = req.body;

        try {
            const response =
                await axios.post('http://localhost:8180/realms/termverse/protocol/openid-connect/token',
                    {
                        client_id: "termverse-app",
                        name: login,
                        password: password,
                        grant_type: "password",
                        client_secret: process.env.KEYCLOAK_CLIENT_SECRET
                    }, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            const accessToken = response.data.access_token;
            const refreshToken = response.data.response_token;
            res.setHeader('Set-Cookie', [
                `termverse_refresh_token=${refreshToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}`,
                `termverse_access_token=${accessToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}`
            ]);
            res.status(200).json({ message: "Success!"});
        } catch (error) {
            console.error('Login error:', error);
            res.status(500).json({ message: "Login Failed" });
        }
    } else {
        res.setHeader('Allow', ['POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}
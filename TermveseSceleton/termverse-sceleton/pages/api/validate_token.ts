import { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { token } = req.body;

    if (!token) {
        return 'none';
    }

    try {
        const response = await axios.post(
            'http://localhost:8180/realms/termverse/protocol/openid-connect/token/introspect',
            {
                token: token,
                client_id: process.env.KEYCLOAK_CLIENT_ID,
                client_secret: process.env.KEYCLOAK_CLIENT_SECRET
            },
             {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

        if (response.data.active) {
            res.status(200).json({ result: 'valid' });
        } else {
            res.status(200).json({ result: 'expired' });
        }
    } catch (error) {
        console.error('Failed to validate token:', error);
        res.status(500).json({ error: 'Failed to validate token' });
    }
}
import type { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    console.log('Receive request: ' + req.url)
    if (req.method === 'POST') {
        const { login: login, password } = req.body;

        try {
            const admin = await axios.post(
                'http://localhost:8180/realms/master/protocol/openid-connect/token',
            {
                        username: process.env.KEYCLOAK_ADMIN_USERNAME,
                        password: process.env.KEYCLOAK_ADMIN_PASSWORD,
                        grant_type: "password",
                        client_id: "admin-cli"
            }, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                }
            );

            console.log('Send admin access request.')

            await axios.post('http://localhost:8180/admin/realms/termverse/users', {
                username: login,
                enabled: true,
                email: login + '@mail.com',
                emailVerified: true,
                credentials: [{
                    type: 'password',
                    value: password,
                    temporary: false
                }]
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${admin.data.access_token}`,
                }
            });

            console.log('Send user create request.')

            const tokenResponse = await axios.post(
                'http://localhost:8180/realms/termverse/protocol/openid-connect/token', {
                    client_id: "termverse-app",
                    username: login,
                    password: password,
                    grant_type: "password",
                    client_secret: process.env.KEYCLOAK_CLIENT_SECRET
                },
                {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            console.log('Send user token access request.')

            const accessToken = tokenResponse.data.access_token;

            res.status(200).json({ accessToken });
        } catch (error) {
            console.error('Error during registration/token retrieval:', error);
            res.status(500).json({ message: "Internal Server Error" });
        }
    } else {
        res.setHeader('Allow', ['POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}
import type { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method === 'POST') {
        const { email, password } = req.body;

        try {
            const javaServerResponse = await axios.post('http://localhost:8080/users', { email, password });
            const userId = javaServerResponse.data.id;

            await axios.post('http://localhost:8180/auth/admin/realms/YourRealm/users', {
                username: email,
                enabled: true,
                attributes: {
                    databaseId: userId
                },
                credentials: [{
                    type: 'password',
                    value: password,
                    temporary: false
                }]
            }, {
                headers: {
                    'Authorization': `Bearer ${process.env.KEYCLOAK_ADMIN_TOKEN}`,
                    'Content-Type': 'application/json'
                }
            });

            res.status(200).json({ success: true, message: "User registered successfully." });
        } catch (error) {
            console.error('Registration error:', error);
            res.status(500).json({ success: false, message: "Internal Server Error" });
        }
    } else {
        res.setHeader('Allow', ['POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}
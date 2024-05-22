import type {NextApiRequest, NextApiResponse} from 'next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    console.log('Receive request: ' + req.url)
    if (req.method === 'POST') {
        const { login: login, password } = req.body;

        try {

            const tokenResponse = await axios.post(
                'http://localhost:8080/register', {
                    login: login,
                    password: password,
                }, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                });

            console.log('Send user token access request.')
            if (tokenResponse.status===200) {
                const accessToken = tokenResponse.data.access_token;
                const refreshToken = tokenResponse.data.refresh_token;

                res.setHeader('Set-Cookie', [
                    `termverse_refresh_token=${accessToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}`,
                    `termverse_access_token=${refreshToken}; Path=/; SameSite=Strict; Max-Age=${30 * 24 * 60 * 60}`
                ]);

                res.status(200).json({accessToken});
            } else {
                res.status(tokenResponse.status).json({ message: tokenResponse.data.error })
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                res.status(error.status || 500).json({ message: error.message})
            } else {
                res.status(500).json({message: "Internal Server Error"});
            }
        }
    } else {
        res.setHeader('Allow', ['POST']);
        res.status(405).end(`Method ${req.method} Not Allowed`);
    }
}
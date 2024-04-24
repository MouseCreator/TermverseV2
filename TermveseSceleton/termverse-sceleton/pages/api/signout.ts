import { NextApiRequest, NextApiResponse } from 'next';

export default function handler(req: NextApiRequest, res: NextApiResponse) {
    res.setHeader('Set-Cookie', [
        'termverse_refresh_token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Strict',
        'termverse_access_token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Strict'
    ]);
    res.status(200).json({ message: 'Signed out successfully' });
}
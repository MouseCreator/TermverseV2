'use client'

import {useActive, useActiveOrRedirect} from "@/ui/utils/isActive";
import Link from "next/link";
export default function Page() {
    let state = useActive();
    if (state === 'loading') {
        return(<div className="p-8">Loading...</div>);
    }
    if (state === 'inactive') {
        return <div  className="p-8">
            <h1> Your session is not active! </h1>
            <Link href="/signin" className="bg-purple-600 rounded text-white w-full h-8 hover:bg-purple-400">
                Sign in
            </Link>
        </div>
    }

    if (state === 'active') {
        return <div className="p-8">
            <h1>Active!</h1>
            <button onClick={signOut} type="button" className="bg-red-500 rounded text-white w-32 h-16 hover:bg-red-400">
                Sign Out
            </button>
        </div>
    }
    return (
        <div>
            <h1>Profile Page</h1>
            {/* Profile content */}
        </div>
    );
};

const signOut = async () => {

    try {
        const response = await fetch('/api/signout', { method: 'POST' });
        const data = await response.json();

        if (!response.ok) {
            console.error('Failed to sign out:', data.message);
        }
    } catch (error) {
        console.error('Failed to sign out:', error);
    }
};
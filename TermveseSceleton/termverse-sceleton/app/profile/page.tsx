'use client'
import { useRouter } from "next/navigation";

import {useActive} from "@/ui/utils/isActive";
import Link from "next/link";
import {useEffect, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
export default function Page() {
    let state = useActive();
    const router = useRouter();
    const [user, setUser] = useState({
         present: false, id: 0, name: ""
    });
    const [error, setError] = useState('');
    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get('http://localhost:8080/users/current'
                ,{ withCredentials: true,});
                if (response.status === 200) {
                    // Assuming the response data includes user details directly
                    setUser({
                        present: true,
                        id: response.data.id,
                        name: response.data.name,
                    });
                } else {
                    setError('Failed to fetch user details: ' + response.status);
                }
            } catch (error) {
                setError('Error fetching user: ' + error);
            }
        };

        fetchUser();
    }, []);
    const signOut = async () => {
        try {
            const response = await fetch('/api/signout', { method: 'POST' });
            const data = await response.json();

            if (!response.ok) {
                console.error('Failed to sign out:', data.message);
            } else {
                router.push('/signin')
            }
        } catch (error) {
            console.error('Failed to sign out:', error);
        }
    };
    if (state === 'loading') {
        return(<div className="p-8 text-2xl">Loading...</div>);
    }
    if (state === 'inactive') {
        return <div  className="p-8">
            <h1 className="text-2xl"> Your session is not active! </h1>
            <Link href="/signin" >
                <div className="bg-purple-600 rounded text-white w-32 h-16 text-center hover:bg-purple-400">
                    Sign in
                </div>
            </Link>
        </div>
    }

    if (state === 'active') {


        return <div className="p-8">
            {
                user.present ? (
                    <h1 className="text-2xl">Hello, {user.name}! Your id is {user.id}</h1>
                ) : (<h1 className="text-2xl">Active!</h1>)
            }

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


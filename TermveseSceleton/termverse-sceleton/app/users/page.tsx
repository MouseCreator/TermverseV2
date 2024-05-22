'use client'
import {User} from "@/ui/components/user";
import axios from "axios";
import Cookies from "js-cookie";
import {UserDescription} from "@/ui/data/data";
import {useEffect, useState} from "react";
export default function Page() {


    const [users, setUsers] = useState<UserDescription[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get<UserDescription[]>('http://localhost:8080/users');
                if (response.status === 200) {
                    setUsers(response.data);
                } else {
                    setError('Failed to fetch users');
                }
            } catch (err: any) {
                setError(err.message || 'An error occurred while fetching users');
            } finally {
                setIsLoading(false);
            }
        };

        fetchUsers();
    }, []);

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <main className="w-full">
            <div className="flex border flex-col items-center">
                {
                    users.map((user, id) => (
                        <User key={"user"+id} props={user}/>
                    ))
                }
            </div>
        </main>
    )
}
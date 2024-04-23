'use client'
import Link from "next/link";
import React, { useState } from 'react';
import axios from "axios";
export function Signup() {

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setIsSubmitting(true);
        try {
            if (password !== repeatPassword) {
                console.log("Passwords do not match")
                return;
            }
            // Step 1: POST request to Java server to create user
            const response = await axios.post('/api/register', {
                name: login,
                profilePictureUrl: null
            });

            const userId = response.data.id;

            // Step 2: POST request to Keycloak server to register user
            const keycloakResponse = await axios.post('http://localhost:8180/admin/realms/termverse/users', {
                username: login,
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
                    'Authorization': `Bearer YOUR_ADMIN_ACCESS_TOKEN`, // Admin token to interact with Keycloak API
                    'Content-Type': 'application/json'
                }
            });

            // Step 3: Log-in the user to receive JWT access token
            const loginResponse = await axios.post('http://localhost:8180/auth/realms/YourRealm/protocol/openid-connect/token', `client_id=your-client-id&username=${login}&password=${password}&grant_type=password`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            const accessToken = loginResponse.data.access_token;
            localStorage.setItem('jwt', accessToken);  // Remember the token in local storage
            console.log('Signup and login successful! Token:', accessToken);
        } catch (error) {
            console.error('An error occurred:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="flex justify-center flex-col h-[90svh]">
            <main className="w-full flex justify-center items-center">
                <div className="bg-gray-100 rounded-2xl p-4">
                <div className="flex flex-col items-center m-8">
                    <h2 className="text-3xl pb-8">Sign up</h2>
                    <form>
                        <div className="flex flex-col items-center pb-4">
                            <label>Login</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="login"/>
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="password"/>
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Confirm password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="confirm-password"/>
                        </div>
                        <div className="pb-4">
                            <button type="submit"
                                    className="bg-purple-600 rounded text-white w-full h-8 hover:bg-purple-400">
                                Submit
                            </button>
                        </div>
                    </form>
                </div>
                <nav className="w-full flex justify-between flex-row px-2">
                    <Link href="/signin" className="text-blue-600">
                        Sign in
                    </Link>
                    <Link href="/" className="text-blue-600">
                        Home
                    </Link>
                </nav>
                </div>
            </main>
        </div>
    )
}
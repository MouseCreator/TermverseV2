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

        if (repeatPassword !== password) {
            console.log("Passwords do not match!")
            return;
        }
        if (isSubmitting) {
            return;
        }
        setIsSubmitting(true);

        try {
            // Receive token
            const registerResponse = await axios.post('/api/register', { login: login, password: password });
            const accessToken = registerResponse.data.accessToken;


            //Store token
            localStorage.setItem('termverse_jwt', accessToken);
            console.log('Token stored:', accessToken);

            //Send to java server
            const javaResponse = await axios.post('http://localhost:8080/users/',
                 {
                    name: login,
                    profilePictureUrl: null
                }, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            });

            console.log('User ID from Java server:', javaResponse.data.id);
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
                    <form onSubmit={handleSubmit}>
                        <div className="flex flex-col items-center pb-4">
                            <label>Login</label>
                            <input className="border-2 border-gray-400 p-1"
                                   autoComplete="off" type="text" name="login"
                                   onChange={(e) => setLogin(e.target.value)} />
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="password"
                                   onChange={(e) => setPassword(e.target.value)}/>
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Confirm password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="confirm-password"
                                   onChange={(e) => setRepeatPassword(e.target.value)}/>
                        </div>
                        <div className="pb-4">
                            <button type="submit" disabled={isSubmitting}
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
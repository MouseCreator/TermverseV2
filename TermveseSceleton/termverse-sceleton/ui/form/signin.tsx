'use client'
import React, { useState } from 'react';
import Link from "next/link";
import axios from 'axios';

export function Signin() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault();
        if (isSubmitting) {
            return;
        }
        setIsSubmitting(true);

        try {
            const response = await axios.post('/api/login', {
                login: login,
                password: password
            });
        } catch (error) {
            console.error('Login error:', error);
        } finally {
            setIsSubmitting(false);
        }
    };


    return (
        <div className="flex justify-center flex-col h-[90svh]">
            <main className="w-full flex justify-center items-center">
                <div className="bg-gray-100 rounded-2xl p-4">
                    <div className="flex flex-col items-center m-8">
                        <h2 className="text-3xl pb-8">Sign in</h2>
                        <form onSubmit={handleLogin}>
                            <div className="flex flex-col items-center pb-4">
                                <label>Login</label>
                                <input className="border-2 border-gray-400 p-1"
                                       autoComplete="off" type="text" name="login"
                                       onChange={(e) => setLogin(e.target.value)}/>
                            </div>
                            <div className="flex flex-col items-center pb-4">
                                <label>Password</label>
                                <input className="border-2 border-gray-400 p-1" autoComplete="off"
                                       type="text" name="password"
                                       onChange={(e) => setPassword(e.target.value)}/>
                            </div>
                            <div className="pb-4">
                                <button type="submit"
                                        className="bg-purple-600 rounded text-white w-full h-8 hover:bg-purple-400"
                                        disabled={isSubmitting}>
                                    Submit
                                </button>
                            </div>
                        </form>
                    </div>
                    <nav className="w-full flex justify-between flex-row px-2">
                        <Link href="/signup" className="text-blue-600">
                            Sign up
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
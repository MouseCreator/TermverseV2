'use client'
import React, {FormEvent, useState} from "react";
import {useRouter} from "next/navigation";
import axios from 'axios';
export default function Page() {

    const router = useRouter();
    const [name, setName] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault(); // Prevent the default form submission behavior

        try {
            const studySetName = (name==="") ? "My study set" : name;
            const response = await axios.post('http://localhost:8080/sets', {
                name: studySetName,
                pictureUrl: null,
                terms: [],
            }, {
                withCredentials: true,  // This option will include cookies with the request
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.status!==200 && response.status!==201) {
                setError("Error while creating study set. Response: " + response.status)
            }

            const data = await response.data;
            const setId = data.id;

            router.push(`/sets/create/${setId}`);
        } catch (error) {
            setError(`Error while creating study set: ${error}`)
        }
    };

    return (
        <main className="w-full flex flex-col items-center h-[60vh] justify-center">
            {
                error && (
                    <div className="rounded-3xl bg-red-400 border-red-500 border-2 p-5 w-1/4" >{error}</div>
                )
            }
            <form className="p-4 flex-col w-1/4" onSubmit={handleSubmit}>
                <h2 className="text-2xl font-bold text-center pb-10">Name Your Study Set</h2>
                <input
                    className="flex-grow p-2 border rounded w-full" autoComplete="off" type="text"
                    id="name"
                    name="name"
                    placeholder="My study set"
                />
                <button type="submit" className="bg-purple-500 text-white p-2 w-full my-2 rounded text-center">Create!</button>
            </form>
        </main>
    )
}
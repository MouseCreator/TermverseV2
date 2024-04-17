'use client';
import React, { useState } from 'react';
import {StudySetCreate, StudySetResponse} from "@/ui/data/data";
import Link from "next/link";
export default function Page() {
    const [formData, setFormData] = useState<StudySetCreate>({
        name: '',
        pictureUrl: null,
    });
    const [errorMessage, setErrorMessage] = useState('');
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/sets', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
            if (response.ok) {
                console.log('Study set created successfully');
                const createdSet: StudySetResponse = await response.json();
                setErrorMessage('Study set created successfully!')
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Error creating study set');
                console.error('Error creating study set:', response.statusText);
            }
        } catch (error) {
            console.error('Error creating study set:', error);
            setErrorMessage('An error occurred while creating the study set');
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    return (
        <div className="w-full p-16 flex flex-row justify-center">
            <form onSubmit={handleSubmit}>
                <label className="error">{errorMessage}</label>
                <div className="p-4">
                    <label htmlFor="name">Name:</label>
                    <input
                        className="border-2 border-gray-400 p-1" autoComplete="off" type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <button type="submit" className="bg-purple-600 rounded text-white w-full h-8 hover:bg-purple-400">Create Study Set</button>
            </form>
            <Link href="/sets/" >
                Back
            </Link>
        </div>
    );
}
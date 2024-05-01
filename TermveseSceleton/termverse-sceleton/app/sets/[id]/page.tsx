'use client'

import { useParams } from 'next/navigation'
import React, { useState, useEffect } from 'react';
import {StudySetResponse, StudySetResponseFull, UserDescription} from "@/ui/data/data";
import Link from "next/link";
import axios from "axios";
import Cookies from "js-cookie";

const StudySetPage = () => {
    const params = useParams<{ id: string; }>()
    const id = params?.id
    const [studySet, setStudySet] = useState<StudySetResponseFull | null>(null);
    const [role, setRole] = useState<string | null>(null);
    const [author, setAuthor] = useState<UserDescription | null>(null)
    useEffect(() => {
        const fetchStudySet = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/sets/full/${id}`);
                const data: StudySetResponseFull = await response.data;
                setStudySet(data);
            } catch (error) {
                console.error('Error fetching study set:', error);
            }
        };
        const fetchAuthor = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/sets/author/${id}`);
                const data: UserDescription = await response.data;
                setAuthor(data);
            } catch (error) {
                console.error('Error fetching study set:', error);
            }
        };

        const fetchRole = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/sets/role/${id}`,
                    {
                        withCredentials: true
                    });
                const data = response.data;
                setRole(data.type);
            } catch (error) {
                console.error('Error fetching study set:', error);
            }
        }

        if (id) {
            fetchStudySet();
            fetchRole();
            fetchAuthor();
        }
    }, [id]);

    if (!studySet) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1 className="text-3xl">Study Set Details</h1>
            <table>
                <thead>
                <tr>
                    <th className="px-4">ID</th>
                    <th className="px-4">Name</th>
                    <th className="px-4">Created</th>
                </tr>
                </thead>
                <tbody>
                <tr className="my-4">
                    <td className="px-4">{studySet.id}</td>
                    <td className="px-4">{studySet.name}</td>
                    <td className="px-4">{studySet.createdAt}</td>
                </tr>
                </tbody>
            </table>
            { author && (<p className="font-bold text-2xl p-4">Created by {author.name }</p>) }
            <table>
                <thead>
                <tr>
                    <th className="px-6">Term</th>
                    <th className="px-6">Definition</th>
                </tr>
                </thead>
            {
                studySet.terms.map((t, i) => (
                    <tr className="my-8 text-center" key={i}>
                        <td>{t.term}</td>
                        <td>{t.definition}</td>
                    </tr>
                ))
            }
            </table>
            <div className="m-12" />
            <div className="flex w-full justify-between">
                <Link href="/sets/" className= "w-16 bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                    Back
                </Link>
                <Link href={`/sets/${id}/flashcards`} className= "w-32 bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                    Flashcards
                </Link>
                {
                    role === 'owner' &&
                    <Link href={`/sets/create/${id}`} className= "w-16 bg-green-600 rounded text-white h-8 hover:bg-green-400">
                        Edit
                    </Link>
                }

            </div>
        </div>
    );
};

export default StudySetPage;
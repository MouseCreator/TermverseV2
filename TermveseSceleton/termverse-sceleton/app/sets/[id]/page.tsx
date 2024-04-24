'use client'

import { useParams } from 'next/navigation'
import React, { useState, useEffect } from 'react';
import {StudySetResponse} from "@/ui/data/data";
import Link from "next/link";
import axios from "axios";
import Cookies from "js-cookie";

const StudySetPage = () => {
    const params = useParams<{ id: string; }>()
    const id = params?.id
    const [studySet, setStudySet] = useState<StudySetResponse | null>(null);
    const [deleteStatus, setDeleteStatus] = useState<'success' | 'error' | null>(null);
    const [role, setRole] = useState<string | null>(null);
    useEffect(() => {
        const fetchStudySet = async () => {
            try {
                const response = await fetch(`http://localhost:8080/sets/${id}`);
                const data: StudySetResponse = await response.json();
                setStudySet(data);
            } catch (error) {
                console.error('Error fetching study set:', error);
            }
        };

        const fetchRole = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/sets/role/${id}`,
                    {
                        headers: {
                            'Authorization': `Bearer ${Cookies.get('termverse_access_token')}`
                        }
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
        }
    }, [id]);
    const handleDelete = async () => {
        try {
            const response = await fetch(`http://localhost:8080/sets/${id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                setDeleteStatus('success');
                // Optionally, you can redirect to another page after successful deletion
                // router.push('/sets');
            } else {
                setDeleteStatus('error');
            }
        } catch (error) {
            console.error('Error deleting study set:', error);
            setDeleteStatus('error');
        }
    };

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
            <div className="flex w-full justify-between">
                <Link href="/sets/" className= "w-16 bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                    Back
                </Link>
                {
                    role === 'owner' ?
                    <button className="w-16 bg-red-600 rounded text-white h-8 hover:bg-purple-400" onClick={handleDelete}>Delete</button>
                        : <div> </div>
                }

            </div>
            {deleteStatus === 'success' && <p>Delete success!</p>}
            {deleteStatus === 'error' && <p>Error deleting study set.</p>}
        </div>
    );
};

export default StudySetPage;
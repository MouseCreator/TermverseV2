'use client'
import axios from "axios";
import {StudySetResponse, StudySetResponseList, UserDescription} from "@/ui/data/data";
import React, {useEffect, useState} from "react";
import {useParams} from "next/navigation";
import {StudSetList} from "@/ui/components/study_set";

export default function Page() {
    const [user, setUser] = useState<UserDescription | null>(null);
    const [error, setError] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(true);

    const params = useParams<{ id: string; }>()
    const id = params?.id
    const [list, setList] = useState<StudySetResponseList>({ list: [] });
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get<UserDescription>(`http://localhost:8080/users/${id}`);
                if (response.status === 200) {
                    setUser(response.data);
                } else {
                    setError('Failed to fetch users');
                }
            } catch (err: any) {
                setError(err.message || 'An error occurred while fetching users');
            } finally {
                setIsLoading(false);
            }
        };

        const fetchUserStudySets = async () => {
            try {
                const response = await axios.get<StudySetResponse[]>(`http://localhost:8080/sets/byuser/${id}`);
                if (response.status === 200) {
                    setList({list: response.data });
                    console.log(list);
                } else {
                    setError('Failed to fetch study sets');
                }
            } catch (err: any) {
                setError(err.message || 'An error occurred while fetching study sets');
            } finally {
                setIsLoading(false);
            }
        };

        fetchUserStudySets();
        fetchUsers();
    }, [id]);
    if (isLoading) {
        return (<div>Loading...</div>)
    }
    if (error) {
        return (<div>error</div>)
    }
    return <div>
        <h1 className="text-2xl text-purple-600 font-bold">{user?.name}</h1>
        {
            list.list ? (<StudSetList props={list}/>) : (<div>User has no study sets</div>)
        }

    </div>
}
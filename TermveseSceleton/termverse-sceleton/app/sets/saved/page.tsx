'use client'
import React, {useEffect, useState} from "react";
import {StudySetResponseList} from "@/ui/data/data";
import Link from "next/link";
import {StudSetList} from "@/ui/components/study_set";

export default function Page() {

    const [list, setList] = useState<StudySetResponseList>({ list: [] });
    const [error, setError] = useState<String>('');
    useEffect(() => {
        onFetch();
    }, []);
    const onFetch = async () => {
        try {
            const data: StudySetResponseList = await fetchSavedSets();
            setList( data );
        } catch (error) {
            setError(`${error}`)
        }
    };
    return (
        <main className="w-full">
            <div className="p-4 flex flex-row justify-around">
                <Link href="/sets/create">
                    <div className="w-32 text-center bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                        Create
                    </div>
                </Link>
                <Link href="/sets">
                    <div className="w-32 text-center bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                        View all
                    </div>
                </Link>
            </div>
            {
               list.list.length > 0 ? ( <StudSetList props={list}/> ) : (<div>No saved study sets</div>)
            }
        </main>
    )
}

async function fetchSavedSets() {
    try {
        const response = await fetch('http://localhost:8080/sets/saved', {
            credentials: "include"
        });
        const data = await response.json();
        return ({ list: data });
    } catch (error) {
        throw new Error("Cannot fetch saved study sets");
    }
    return ({ list: Array.of()})
}
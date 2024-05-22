'use client'
import {StudSetList} from "@/ui/components/study_set";
import { StudySetResponseList} from "@/ui/data/data";
import React, { useEffect, useState } from 'react';
import {fetchAllSets} from "@/ui/data/sets";
import Link from "next/link";

export default function Page() {

    const [list, setList] = useState<StudySetResponseList>({ list: [] });

    useEffect(() => {
        onFetch();
    }, []);
    const onFetch = async () => {
        try {
            const data: StudySetResponseList = await fetchAllSets();
            setList( data );
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    return (
        <main className="w-full">
            <div className="p-4 flex flex-row justify-around">
                <Link href="sets/create">
                    <div className="w-32 text-center bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                    Create
                    </div>
                </Link>
                <Link href="sets/saved">
                    <div className="w-32 text-center bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                        View saved
                    </div>
                </Link>
            </div>
            <StudSetList props={list}/>
        </main>
    )
}
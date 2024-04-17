'use client'
import {StudSetList} from "@/ui/components/study_set";
import {StudySetPropsList} from "@/ui/data/data";
import React, { useEffect, useState } from 'react';
import {fetchAllSets} from "@/ui/data/sets";

export default function Page() {

    const [list, setList] = useState<StudySetPropsList>({ list: [] });

    useEffect(() => {
        onFetch();
    }, []);
    const onFetch = async () => {
        try {
            const data: StudySetPropsList = await fetchAllSets();
            setList(data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    return (
        <main className="w-full">
            <StudSetList props={list}/>
        </main>
    )
}
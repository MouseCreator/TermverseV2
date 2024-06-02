'use client'
import {StudSetList, StudSetListCombined} from "@/ui/components/study_set";
import {StudySetCombinedList, StudySetResponseList} from "@/ui/data/data";
import React, { useEffect, useState } from 'react';
import {fetchAllSets} from "@/ui/data/sets";
import Link from "next/link";
import axios from "axios";
import {list} from "postcss";
export default function Page() {

    const [sets, setSets] = useState<StudySetCombinedList>({list: []});
    const [page, setPage] = useState(1);
    const [search, setSearch] = useState('');
    const [category, setCategory] = useState('Saved');
    const [sort, setSort] = useState('Name');
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        fetchSets();
        fetchTotalPages();
    }, [page, search, category, sort]);

    function mapCategory(category: string) {
        if (category === "All") return "all";
        if (category === "My study sets") return "my_sets";
        if (category === "Saved") return "saved";
        return "all";
    }

    const fetchSets = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/sets/search`, {
                params: {
                    page: page-1,
                    size: 10,
                    q: search.trim(),
                    sort: sort.toLowerCase(),
                    category: mapCategory(category)
                },
                withCredentials: true
            });
            setSets({list: response.data});
        } catch (error) {
            console.error('Error fetching sets:', error);
        }
    };

    const fetchTotalPages = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/sets/search/total`, {
                params: {
                    page: page-1,
                    size: 10,
                    q: search.trim(),
                    sort: sort.toLowerCase(),
                    category: mapCategory(category)
                },
                withCredentials: true
            });
            setTotalPages(response.data.totalPages);
            console.log(totalPages)
        } catch (error) {
            console.error('Error fetching sets:', error);
        }
    };

    return (
        <div className="container mx-auto p-4 w-full ">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 w-full">
                {/* Options Column */}
                <div className="col-span-1">
                    <div className="mb-4">
                        <input
                            type="text"
                            placeholder="Search"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                            className="w-full p-2 border rounded"
                        />
                    </div>
                    <div className="mb-4">
                        <h2 className="text-lg font-semibold mb-2">Categories</h2>
                        <ul>
                            {['Saved', 'My study sets', 'All'].map((cat) => (
                                <li key={cat}>
                                    <button
                                        onClick={() => setCategory(cat)}
                                        className={`w-full text-left p-2 ${category === cat ? 'bg-gray-200' : ''}`}
                                    >
                                        {cat}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div className="mb-4">
                        <h2 className="text-lg font-semibold mb-2">Sort by</h2>
                        <ul>
                            {['Name', 'Recent', 'Oldest'].map((sortOption) => (
                                <li key={sortOption}>
                                    <button
                                        onClick={() => setSort(sortOption)}
                                        className={`w-full text-left p-2 ${sort === sortOption ? 'bg-gray-200' : ''}`}
                                    >
                                        {sortOption}
                                    </button>
                                </li>
                            ))}
                        </ul>
                        <Link href="/sets/create">
                            <div className="w-1/2 text-center bg-purple-600 rounded text-white h-8 hover:bg-purple-400">
                                Create Study Set
                            </div>
                        </Link>
                    </div>
                </div>

                {/* Sets Column */}
                <div className="col-span-3 w-full">
                    {/* Pagination */}
                    <div className="flex justify-center mt-4">
                        <button
                            onClick={() => setPage((prev) => Math.max(prev - 1, 1))}
                            disabled={page === 1}
                            className="p-2 border rounded-l disabled:bg-slate-50"
                        >
                            Previous
                        </button>
                        <span className="p-2 px-6 border-t border-b">{page}</span>
                        <button
                            onClick={() => setPage((prev) => Math.min(prev + 1, totalPages))}
                            disabled={page === totalPages}
                            className="p-2 border rounded-r disabled:bg-slate-50"
                        >
                            Next
                        </button>
                    </div>
                    <div className="md:grid-cols-2 lg:grid-cols-3 gap-4 w-full">
                        <StudSetListCombined props={ sets }/>
                    </div>

                </div>
            </div>
        </div>
    );
}
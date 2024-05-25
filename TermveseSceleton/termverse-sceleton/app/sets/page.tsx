'use client'
import {StudSetList} from "@/ui/components/study_set";
import { StudySetResponseList} from "@/ui/data/data";
import React, { useEffect, useState } from 'react';
import {fetchAllSets} from "@/ui/data/sets";
import Link from "next/link";
import axios from "axios";
export default function Page() {

    const [sets, setSets] = useState([]);
    const [page, setPage] = useState(1);
    const [search, setSearch] = useState('');
    const [category, setCategory] = useState('Saved');
    const [sort, setSort] = useState('Name');
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        fetchSets();
    }, [page, search, category, sort]);

    const fetchSets = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/sets`, {
                params: {
                    page,
                    size: 10,
                    search,
                    sort,
                    category
                }
            });
           // setSets(response.data.sets);
           // setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching sets:', error);
        }
    };

    return (
        <div className="container mx-auto p-4">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
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
                    </div>
                </div>

                {/* Sets Column */}
                <div className="col-span-3">
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {sets.map((set) => (
                            <div key={set.id} className="p-4 border rounded">
                                {/* Display set details */}
                                <h3 className="text-xl font-semibold">{set.name}</h3>
                                {/* Other details can be added here */}
                            </div>
                        ))}
                    </div>
                    {/* Pagination */}
                    <div className="flex justify-center mt-4">
                        <button
                            onClick={() => setPage((prev) => Math.max(prev - 1, 1))}
                            disabled={page === 1}
                            className="p-2 border rounded-l"
                        >
                            Previous
                        </button>
                        <span className="p-2 border-t border-b">{page}</span>
                        <button
                            onClick={() => setPage((prev) => Math.min(prev + 1, totalPages))}
                            disabled={page === totalPages}
                            className="p-2 border rounded-r"
                        >
                            Next
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
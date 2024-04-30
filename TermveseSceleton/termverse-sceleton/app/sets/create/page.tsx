'use client';
import React, { useState } from 'react';
import {StudySetCreate, StudySetResponse, TermCreateDTO, TermResponseDTO} from "@/ui/data/data";
import Link from "next/link";
import Cookies from "js-cookie";
import axios from "axios";
import {useRouter} from "next/navigation";
import TermForm from "@/ui/components/TermForm";
import {DragDropContext, Droppable, Draggable, OnDragEndResponder, DropResult} from 'react-beautiful-dnd';

interface TempTerm {
    id: string,
    term: string,
    definition: string
}
export default function Page() {
    const router = useRouter();
    const [terms, setTerms] = useState<TempTerm[]>([]);
    const handleAddTerm = () => {
        setTerms([...terms, { id: `T- ${Date.now()}`, term: '', definition: '' }]);
    };
    const [formData, setFormData] = useState<StudySetCreate>({
        name: '',
        pictureUrl: null,
        terms: []
    });
    const [errorMessage, setErrorMessage] = useState('');

    const handleDelete = () => {

    }
    const handleSubmit = async () => {
        try {
            const response = await axios.post('http://localhost:8080/sets', formData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': Cookies.get('termverse_access_token'),
                    }
                });
            if (response.status===201 || response.status===200) {
                console.log('Study set created successfully');
                const createdSet: StudySetResponse = await response.data;
                router.push('/sets')
            } else {
                const errorData = await response.data;
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
    const handleDeleteTerm = (id: string) => {
        setTerms(terms.filter(term => term.id !== id));
    };
    const handleDragEnd = (result: DropResult) => {
        if (!result.destination) return;
        const items = Array.from(terms);
        const [reorderedItem] = items.splice(result.source.index, 1);
        items.splice(result.destination.index, 0, reorderedItem);
        setTerms(items);
    };

    const getListStyle = (isDraggingOver: boolean) => ({
        background: isDraggingOver ? "#F0F0F0" : "white",
        borderRadius: 25,
        paddingLeft: 4,
        paddingRight: 4,
        minHeight: 30,
        minWidth: "35vw"
    });

    const getItemBackground = (isDragging: boolean): string => {
        return (isDragging ? "#D1EBFF" : "white");
    };

    return (
        <div className="w-full px-2 justify-center ">
            <div className="w-full">
                <label className="error">{errorMessage}</label>
                <main className="w-full flex flex-row">
                    <div className="w-1/4">
                        <div className="p-4">
                            <label htmlFor="name">Study Set Name:</label>
                            <input
                                className="flex-grow p-2 border w-full rounded p-1" autoComplete="off" type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="bg-white p-4 shadow-md flex justify-between">
                            <div className="flex flex-row w-full">
                                <div className="flex flex-col w-1/2">
                                    <button onClick={handleSubmit} className="bg-green-500 text-white px-4 py-2 m-4 rounded text-center">
                                        Save
                                    </button>
                                    <button onClick={handleAddTerm} className="bg-purple-500 text-white px-4 py-2 rounded m-4 text-center">
                                        New Term
                                    </button>
                                    <Link href="/sets/" >
                                        <div className="bg-purple-500  text-white px-4 py-2 rounded m-4 text-center">
                                            Back
                                        </div>
                                    </Link>
                                    <button onClick={()=>{}} className="bg-red-500 text-white px-4 py-2 rounded m-4 text-center">
                                        Delete
                                    </button>
                                </div>
                                <div className="w-1/2 p-4">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="flex flex-col w-3/4  items-center">
                        <p className="px-4 text-center" >Terms. Total: {terms.length}</p>
                        <div className=" overflow-y-auto h-[70vh]">
                            <div className="flex flex-col space-y-4 ">
                                <DragDropContext onDragEnd={handleDragEnd}>
                                    <Droppable droppableId="terms">
                                        {(provided, snapshot) => (
                                            <div
                                                {...provided.droppableProps}
                                                ref={provided.innerRef}
                                                style={getListStyle(snapshot.isDraggingOver)}
                                            >
                                                 {terms.map((term, index) => (
                                                     <Draggable key={term.id} draggableId={term.id.toString()} index={index}>
                                                         {(provided, snapshot) => (
                                                             <div
                                                                 ref={provided.innerRef}
                                                                 {...provided.draggableProps}
                                                                 style={{ ...provided.draggableProps.style }}
                                                             >
                                                                 <div {...provided.dragHandleProps}>
                                                                     <TermForm id={term.id} onDelete={handleDeleteTerm} bg={getItemBackground(snapshot.isDragging)} />
                                                                 </div>
                                                             </div>
                                                         )}
                                                     </Draggable>
                                                 ))}
                                                     {provided.placeholder}
                                            </div>
                                        )}
                                    </Droppable>
                                </DragDropContext>
                            </div>
                        </div>
                    </div>
                </main>
            </div>

        </div>
    );
}

function mapToTerms(temps: TempTerm[]): TermCreateDTO[] {
    return temps.map((t, i): TermCreateDTO => {
        return {
            term: t.term,
            definition: t.definition,
            order: i+1,
            picture_url: null,
            hint: null
        }
    })
}

function mapFromTerms(temps: TermResponseDTO[]): TempTerm[] {
    return temps.sort((t1, t2) => t1.order - t2.order) .map((t): TempTerm => {
        return {
            id: "T-" + Date.now(),
            term: t.term,
            definition: t.definition,
        }
    })
}
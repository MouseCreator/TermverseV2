'use client';
import React, { useState, useEffect } from 'react';
import {StudySetResponseFull, TermCreateDTO, TermResponseDTO} from "@/ui/data/data";
import Link from "next/link";
import axios, {AxiosResponse} from "axios";
import {useRouter, useParams } from "next/navigation";
import {DragDropContext, Droppable, Draggable, DropResult} from 'react-beautiful-dnd';
import Image from "next/image";

interface TempTerm {
    id: string,
    term: string,
    definition: string
}
export default function Page() {
    const router = useRouter();
    const [terms, setTerms] = useState<TempTerm[]>([]);
    const [error, setError] = useState<String>("");
    const [errorCauser, setErrorCauser] = useState<String | null>(null);
    const handleAddTerm = () => {
        setTerms([...terms, { id: `T-${Date.now()}`, term: '', definition: '' }]);
    };
    const [name, setName] = useState<string>("");
    const params = useParams<{ id: string; }>()
    const tempId = params?.id;

    const setId = tempId ? Number.parseInt(tempId) : undefined;
    useEffect(() => {
        if (setId) {
            try {
                fetchStudySet(setId, setName, setTerms);
                setError("")
            } catch (error) {
                setError(`${error}`)
            }
        }
    }, [setId]);


    const handleDelete = async () => {
        const success = await deleteStudySet(setId);
        if (success) {
            router.push('/sets/')
        } else {
            setError("Error deleting study set")
        }
    }
    const findErrorCauser = () => {
        return terms.findIndex(t => t.term.trim()==="" || t.definition.trim()==="");
    }
    const handleSubmit = async () => {
        if (!setId) return;
        try {
            if (name.trim()==="") {
                return;
            }
            const errC = findErrorCauser()
            if (errC !== -1) {
                triggerError (`Term # ${errC+1} is incomplete`)
                setErrorCauser(terms[errC].id)
                return;
            }
            const updated = await updateStudySet(setId, name, terms)
            setName(updated.name)
            setTerms(updated.terms);
            triggerError ("Saved!")
        } catch (error) {
            triggerError (String(error))
        }

    };
    const handleDeleteTerm = (index: number) => {
        setTerms(terms.filter((term, termIndex) => termIndex !== index));
    };
    const handleDragEnd = (result: DropResult) => {
        if (!result.destination) return;
        const items = Array.from(terms);
        const [reorderedItem] = items.splice(result.source.index, 1);
        items.splice(result.destination.index, 0, reorderedItem);
        setTerms(items);
    };
    const [showError, setShowError] = useState(false);

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

    const handleDefinitionChange = (index: number, newDefinition: string) => {
        const newTerms = terms.map((term, i) => {
            if (i === index) {
                return { ...term, definition: newDefinition };
            }
            return term;
        });
        setTerms(newTerms);
    };
    const triggerError = (msg: string) => {
        setError(msg);
        setShowError(true);
        setTimeout(() => {
            setShowError(false);
        }, 5000);
    };
    const handleTermChange = (index: number, newTerm: string) => {
        const newTerms = terms.map((term, i) => {
            if (i === index) {
                return { ...term, term: newTerm };
            }
            return term;
        });
        setTerms(newTerms);
    };

    return (
        <div className="w-full px-2 justify-center ">
            <div className="w-full">
                <main className="w-full flex flex-row">
                    <div className="w-1/4">
                        <div className="p-4">
                            <label htmlFor="name">Study Set Name:</label>
                            <input
                                className={`flex-grow p-2 border w-full rounded ${name.trim()==="" && "border-red-600"}`} autoComplete="off" type="text"
                                id="name"
                                name="name"
                                value={name}
                                onChange={e => setName(e.target.value)}
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
                                    <Link href={"/sets/"+setId} >
                                        <div className="bg-purple-500  text-white px-4 py-2 rounded m-4 text-center">
                                            Back
                                        </div>
                                    </Link>
                                    <button onClick={handleDelete} className="bg-red-500 text-white px-4 py-2 rounded m-4 text-center">
                                        Delete
                                    </button>
                                </div>
                                <div className="w-1/2 p-4">{showError && error}</div>
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
                                                     <Draggable key={index} draggableId={index.toString()} index={index}>
                                                         {(provided, snapshot) => (
                                                             <div
                                                                 ref={provided.innerRef}
                                                                 {...provided.draggableProps}
                                                                 style={{ ...provided.draggableProps.style }}
                                                             >
                                                                 <div {...provided.dragHandleProps}>
 <div className="border-2 border-gray-200 px-10 py-3 rounded-2xl my-2 w-[32vw]"
      style={{
          background: getItemBackground(snapshot.isDragging)
      }}>
     <div className={`flex items-center space-x-4 ${ term.id===errorCauser ? 'bg-red-200' : ''}`}>
         <input className="flex-grow p-2 border rounded" placeholder="Term"
         onChange={(e) => handleTermChange(index, e.target.value)}
         value={terms[index].term}/>
         <input className="flex-grow p-2 border rounded" placeholder="Definition"
         onChange={(e) => handleDefinitionChange(index, e.target.value)}
         value={terms[index].definition}/>
         <button onClick={() => handleDeleteTerm(index)} className="w-8 h-8">
             <Image src="/trash.svg" alt="Delete" width="32" height="32" />
         </button>
     </div>
 </div>
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

async function fetchStudySet(id: number, setName: (name: string) => void, setTerms: (terms: TempTerm[]) => void) {
    const response: AxiosResponse<StudySetResponseFull> = await axios.get('http://localhost:8080/sets/full/'+id,  {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (response.status !== 200) {
        throw new Error("Cannot get study set from database")
    }
    const data = response.data;
    setName(data.name)
    setTerms(mapFromTerms(data.terms))
}

async function deleteStudySet(setId: number | undefined) {
    if (!setId) {
        return false;
    }
    const response: AxiosResponse<StudySetResponseFull> = await axios.delete('http://localhost:8080/sets/'+setId,  {
        withCredentials: true,
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return response.status === 200;

}
interface StudySetData {
    name: string,
    terms: TempTerm[]
}
async function updateStudySet(id: number, name: String, temps: TempTerm[]): Promise<StudySetData> {
    const termCreateDTOS = mapToTerms(temps);
    try {
        const response: AxiosResponse<StudySetResponseFull> = await axios.put('http://localhost:8080/sets/' + id,
            {
                id: id,
                name: name,
                pictureUrl: null,
                terms: termCreateDTOS
            }, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json'
                }
            });
        if (response.status === 401 || response.status === 403) {
            throw new Error("You don't have permission to edit this study set")
        }
        if (response.status !== 200) {
            throw new Error("Cannot update study set!")
        }
        return {
            name: response.data.name,
            terms: mapFromTerms(response.data.terms)
        }
    } catch (error) {
        throw new Error(`Cannot save study set: ${error}`)
    }



}
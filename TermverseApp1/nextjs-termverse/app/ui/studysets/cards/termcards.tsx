'use client'
import {useState} from 'react';
import {TermsProps} from "@/app/ui/studysets/termtable";
import Image from "next/image";

export default function Flashcards({ terms }: TermsProps){
    const [currentIndex, setCurrentIndex] = useState(0);
    const [showDefinition, setShowDefinition] = useState(false);
    const handlePrevClick = () => {
        setCurrentIndex((prevIndex) => prevIndex - 1);
        setShowDefinition(false);
    };

    const handleNextClick = () => {
        setCurrentIndex((prevIndex) => prevIndex + 1);
        setShowDefinition(false);
    };

    const handleCardClick = () => {
        setShowDefinition((prevState) => !prevState);
    };

    const currentTerm = terms[currentIndex];

    return (
        <div>
            <div className="flex justify-center items-center text-3xl mb-4">
                <div className="p-2 overflow-hidden select-none">
                    <div
                    className="border-2 bg-white border-purple-500 shadow-lg p-4 cursor-pointer w-[80vw] max-w-full h-[70vh] flex items-center justify-center"
                    onClick={handleCardClick}
                    >
                    <div className="flex justify-between items-center w-full">
                        <div className = {currentTerm.picture_url ? "w-1/2" : "w-full"}>
                            <p className="w-full p-4 text-center pointer-events-none">{showDefinition ? currentTerm.definition : currentTerm.term}</p>
                        </div>
                        {currentTerm.picture_url &&
                            <div className="w-1/2 flex justify-center">
                                <Image className="rounded w-1/2 image-fit pointer-events-none"
                                                                   src={currentTerm.picture_url}
                                                                   alt={currentTerm.term}
                                                                   width={512}
                                                                   height={512}
                                                                   objectFit="cover"
                                />
                            </div>
                        }
                    </div>
                    </div>
                </div>
            </div>
            <div className="flex justify-center items-start">
                <button
                    className={`mr-4 ${
                        currentIndex > 0 ? 'text-purple-500 cursor-pointer' : 'text-gray-400'
                    }`}
                    onClick={handlePrevClick}
                    disabled={currentIndex === 0}
                >
                    {'<'}
                </button>
                <button
                    className={`ml-4 ${
                        currentIndex < terms.length - 1 ? 'text-purple-500 cursor-pointer' : 'text-gray-400'
                    }`}
                    onClick={handleNextClick}
                    disabled={currentIndex === terms.length - 1}
                >
                    {'>'}
                </button>
            </div>
        </div>
    );
};
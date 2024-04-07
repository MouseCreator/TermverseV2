'use client'
import React, {useState} from "react";
import {TermsProps} from "@/app/ui/studysets/termtable";
import Image from "next/image";


export default function WriteForm(termsProps : TermsProps) {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [correctCount, setCorrectCount] = useState(0);
    const [currentValue, setCurrentValue] = useState("");
    const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
    const terms = termsProps.terms;
    const handleTermSubmit = () => {
        console.log('Click')
        const isAnswerCorrect = currentValue?.toString().toLowerCase() === terms[currentIndex].term.toLowerCase();
        setIsCorrect(isAnswerCorrect);
        if (isAnswerCorrect) {
            setCorrectCount(correctCount + 1);
        }
        setCurrentIndex(currentIndex + 1);
        setCurrentValue('');
    };
    const totalSize = terms.length;
    const progressPercentage = (currentIndex / totalSize) * 100;

    if (currentIndex === terms.length) {
        return (
            <div className="text-center">
                <h1 className="text-2xl font-bold mb-4">Writing App</h1>
                <p className="text-lg">You have completed all the terms.</p>
                <p className="text-lg">
                    Total correct terms: {correctCount} out of {terms.length}
                </p>
            </div>
        );
    }

    const currentTerm = terms[currentIndex];

    return (
        <div>
            <div className="flex h-[50vh] mx-8">
                <div className="flex justify-between items-center w-full">
                    <div className = {currentTerm.picture_url ? "w-1/2" : "w-full"}>
                        <p className="w-full p-4 pointer-events-none">{currentTerm.definition}</p>
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
            <div className ="mx-8">
                <div>
                    <input
                        autoComplete="off"
                        type="text"
                        name="answer"
                        value={currentValue}
                        onChange={(e) => setCurrentValue(e.currentTarget.value)}
                        className="border border-gray-300 rounded px-2 py-1 mb-4"
                    />
                    <button className="bg-purple-500 text-white px-4 py-2 rounded" onClick={handleTermSubmit}>
                        Submit
                    </button>
                    {isCorrect !== null && (
                        <p className={`text-lg ${isCorrect ? 'text-green-500' : 'text-red-500'}`}>
                            {isCorrect ? 'Correct' : 'Wrong'}
                        </p>
                    )}
                </div>
                <div className="sm:w-96 md:w-1/2 lg:w-1/3 xl:w-1/4">
                    <ProgressBar progressPercentage={progressPercentage} currentIndex={currentIndex} totalSize={totalSize}/>
                </div>
            </div>
        </div>
    );
}
interface PBProps {
    progressPercentage: number,
    currentIndex: number,
    totalSize: number
}
export function ProgressBar(props: PBProps) {
    return (
        <div className="relative w-full mb-4">
            <div className="overflow-hidden h-2 text-xs flex rounded bg-gray-500">
                <div
                    style={{ width: `${props.progressPercentage}%` }}
                    className="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-purple-600"
                ></div>
            </div>
            <div className="text-sm text-gray-600 mt-1">
                {props.currentIndex} / {props.totalSize}
            </div>
        </div>
    );
}
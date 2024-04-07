'use client'
import React, {useState} from "react";
import {TermsProps} from "@/app/ui/studysets/termtable";


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
        <div className="text-center">
            <h1 className="border-red-500 border-2">{new Date(Date.now()).getSeconds()}</h1>
            <p className="text-lg mb-4">{currentTerm.definition}</p>
            {currentTerm.picture_url && (
                <img src={currentTerm.picture_url} alt="Term Picture" className="mx-auto mb-4" />
            )}
            <input
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
    );
}
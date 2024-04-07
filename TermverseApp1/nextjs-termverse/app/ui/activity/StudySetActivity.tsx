
export function StudySetActivity() {
    return (
        <div className="grid grid-cols-3 grid-gap-4 p-8 w-full place-items-center">
           <div className="bg-gray-300 w-64 h-20 flex justify-center text-bold text-lg items-center">Flashcards</div>
           <div className="bg-gray-300 w-64 h-20 flex justify-center text-bold text-lg items-center">Learn</div>
           <div className="bg-gray-300 w-64 h-20 flex justify-center text-bold text-lg items-center">Write</div>
        </div>
    );
}
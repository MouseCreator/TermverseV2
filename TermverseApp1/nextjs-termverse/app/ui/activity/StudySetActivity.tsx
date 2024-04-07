
export function StudySetActivity() {
    return (
        <div className="grid grid-cols-3 grid-gap-4 px-8 py-28 w-full place-items-center">
           <div className="bg-purple-500 rounded w-64 font-bold
           h-24 flex justify-center text-bold text-white text-lg items-center">Flashcards</div>
           <div className="bg-purple-500 rounded w-64 font-bold
           h-24 flex justify-center text-bold text-white text-lg items-center">Learn</div>
           <div className="bg-purple-500 rounded w-64 font-bold
           h-24 flex justify-center text-bold text-white text-lg items-center">Write</div>
        </div>
    );
}

export function StudySetActivity() {
    return (
        <div className="grid xl:grid-cols-3 lg:grid-cols-2 lg:grid-cols-3 grid-cols-1 gap-4 px-8 py-28 w-full place-items-center">
           <div className="bg-purple-500 rounded lg:w-64 w-full font-bold p-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Flashcards</div>
           <div className="bg-purple-500 rounded lg:w-64 w-full font-bold p-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Learn</div>
           <div className="bg-purple-500 rounded lg:w-64 w-full font-bold p-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Write</div>
        </div>
    );
}
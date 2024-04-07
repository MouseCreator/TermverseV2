
export function StudySetActivity() {
    return (
        <div className="grid xl:grid-cols-3 lg:grid-cols-3 grid-cols-1 gap-4 px-8 py-28 w-full place-items-center">
           <button className="bg-purple-500 hover:bg-purple-700 rounded xl:w-64 lg:w-42 w-full font-bold mx-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Flashcards</button>
           <button className="bg-purple-500 hover:bg-purple-700 rounded xl:w-64 lg:w-42 w-full font-bold mx-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Learn</button>
           <button className="bg-purple-500 hover:bg-purple-700 rounded xl:w-64 lg:w-42 w-full font-bold mx-3
           h-24 flex justify-center text-bold text-white text-lg items-center">Write</button>
        </div>
    );
}
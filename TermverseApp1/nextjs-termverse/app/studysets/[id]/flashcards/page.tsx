import {getStudySetTermsFromServer} from "@/app/ui/studysets/termtable";
import Flashcards from "@/app/ui/studysets/cards/termcards";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const termsList = await getStudySetTermsFromServer(param)
    return (<div>
        <div className="flex justify-between mt-4">
            <h1 className="mx-4 text-2xl font-bold">Study set</h1>
            <button className="bg-green-600 text-white rounded font-bold w-32 h-12 mx-4">Done</button>
        </div>
        <Flashcards terms={termsList.terms}/>


    </div>);
}
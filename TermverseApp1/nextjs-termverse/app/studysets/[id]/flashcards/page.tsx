import {getStudySetTermsFromServer} from "@/app/ui/studysets/termtable";
import Flashcards from "@/app/ui/studysets/cards/termcards";
import Link from "next/link";
import {getStudySetGenericInfo} from "@/app/ui/studysets/headdata";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const termsList = await getStudySetTermsFromServer(param)
    const studySetGeneralInfo = await getStudySetGenericInfo(param)
    return (<div>
        <div className="flex justify-between mt-4 items-center">
            <h1 className="mx-4 text-2xl font-bold">{studySetGeneralInfo.studySet.name}</h1>
            <Link href={`/studysets/${param}`}>
                <button className="bg-green-600 hover:bg-green-700 text-white rounded font-bold w-32 h-12 mx-4">Done</button>
            </Link>
        </div>
        <Flashcards terms={termsList.terms}/>


    </div>);
}
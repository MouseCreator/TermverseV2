import Flashcards from "@/ui/components/termcards";
import Link from "next/link";
import axios from "axios";
import {StudySetResponseFull} from "@/ui/data/data";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const studySet = await getStudySetTermsFromServer(param)
    if (!studySet) {
        return <div>Loading...</div>
    }
    return (<div>
        <div className="flex justify-between mt-4 items-center">
            <h1 className="mx-4 text-2xl font-bold">{studySet.name}</h1>
            <Link href={`/sets/${param}`}>
                <button className="bg-green-600 hover:bg-green-700 text-white rounded font-bold w-32 h-12 mx-4">Done</button>
            </Link>
        </div>
        <Flashcards terms={studySet.terms}/>

    </div>);
}

async function getStudySetTermsFromServer(id: number) {
    try {
        const response = await axios.get(`http://localhost:8080/sets/full/${id}`);
        const data: StudySetResponseFull = await response.data;
        return data;
    } catch (error) {
        console.error('Error fetching study set:', error);
    }
}
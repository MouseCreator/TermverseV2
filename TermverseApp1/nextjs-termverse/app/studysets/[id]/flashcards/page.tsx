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
        <Flashcards terms={termsList.terms}/>
    </div>);
}
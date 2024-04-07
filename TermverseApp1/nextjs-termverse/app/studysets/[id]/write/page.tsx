import {getStudySetTermsFromServer, TermsProps} from "@/app/ui/studysets/termtable";
import {PageProps} from "@/app/ui/data/Utils";
import WriteForm from "@/app/ui/write/WriteForm";
import {getStudySetHeadFromServer} from "@/app/ui/studysets/headdata";

export default async function Page({params} : PageProps) {
    const param = Number.parseInt(params.id);
    const studySetHead = await getStudySetHeadFromServer(param)
    const {terms} = await getStudySetTermsFromServer(param)
    const shuffledTerms = terms.sort(() => Math.random() - 0.5);
    return (
        <main>
            <h1>{studySetHead.headData.name}</h1>
            <WriteForm terms={shuffledTerms} />
        </main>
    );
}

import {HeadData, HeadDataI, getStudySetHeadFromServer, HeadDataProps} from "@/app/ui/studysets/headdata";
import {getStudySetTermsFromServer, TermsTable} from "@/app/ui/studysets/termtable";
import {StudySetActivity} from "@/app/ui/activity/StudySetActivity";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const studySetHead = await getStudySetHeadFromServer(param)
    const termsList = await getStudySetTermsFromServer(param)
    return (
        <div>
            <div className="flex">
                <main className="flex min-h-screen flex-col justify-between p-12 w-full">
                    <HeadData headData={ studySetHead.headData }/>
                    <StudySetActivity/>
                    <TermsTable terms={ termsList.terms }/>
                </main>
            </div>

        </div>
    );
}

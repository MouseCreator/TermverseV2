import {getStudySetHeadFromServer} from "@/app/ui/studysets/headdata";
import {getUserData} from "@/app/ui/users/userhead";
import {Calendar} from "@/app/ui/users/calendar";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const userInfo = await getUserData(param)
    return <div>
        {userInfo.id}
        <Calendar />
    </div>
}
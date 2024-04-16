import {getUserData, UserHead} from "@/app/ui/users/userhead";
import {Calendar} from "@/app/ui/users/calendar";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const userInfo = await getUserData(param)
    return <div className="mx-auto">
        <UserHead user={userInfo}/>
        <main>
            <div className="grid grid-cols-2 gap-4">
                <Calendar />
                <div className="grid grid-cols-2">
                    <div className="bg-gray-300 w-96 h-32">Study Sets</div>
                    <div className="bg-gray-300 w-96 h-32">Study Sets</div>
                    <div className="bg-gray-300 w-96 h-32">Study Sets</div>
                    <div className="bg-gray-300 w-96 h-32">Study Sets</div>
                </div>
            </div>
        </main>

    </div>
}
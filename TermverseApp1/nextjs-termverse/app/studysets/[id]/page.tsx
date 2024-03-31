
import {HeadData, getServerSideProps} from "@/app/ui/studysets/headdata";

interface PageProps {
    params: {
        id: string;
    };
}
export default async function Page({ params }: PageProps) {
    const param = Number.parseInt(params.id);
    const studySetHead = await getServerSideProps(param)
    return (
        <div>
            <div className="flex">
                <main className="flex min-h-screen flex-col items-center justify-between p-12 w-full">
                    <HeadData headData={ studySetHead.headData }/>
                </main>
            </div>

        </div>
    );
}
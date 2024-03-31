import {HeadData, getServerSideProps} from "@/app/ui/studysets/headdata";

export default async function Page() {
    const studySetHead = await getServerSideProps()
    return (
        <div>
            <div className="flex">
                <main className="flex min-h-screen flex-col items-center justify-between p-24">
                    <HeadData headData={ studySetHead.headData }/>
                </main>
            </div>

        </div>
    );
}
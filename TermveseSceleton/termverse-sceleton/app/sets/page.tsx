import {StudySet} from "@/ui/components/study_set";


export default function Page() {
    return (

        <main className="w-full">
            <div className="flex border flex-col items-center">
                <StudySet props={
                    {id: 1, name: "Set 1", created_at: new Date(Date.now()), picture_url: null, size: 3}
                } />
                <StudySet props={
                    {id: 2, name: "Set 2", created_at: new Date(Date.now()), picture_url: null, size: 3}
                } />
                <StudySet props={
                    {id: 3, name: "Set 3", created_at: new Date(Date.now()), picture_url: null, size: 3}
                } />
            </div>
        </main>
    )
}
import Link from "next/link";
import {IdHolder} from "@/app/ui/data/Utils";

export function StudySetActivity(param: IdHolder) {
    const baseUrl = `/studysets/${param.id}/`
    return (
        <div className="grid xl:grid-cols-3 lg:grid-cols-3 grid-cols-1 gap-4 px-8 py-28 w-full place-items-center">
            <LinkButton text="Flashcards" url={baseUrl+'flashcards'}/>
            <LinkButton text="Learn" url={baseUrl+'learn'}/>
            <LinkButton text="Write" url={baseUrl+'write'}/>
        </div>
    );
}
interface LinkButtonProps {
    text: string,
    url: string
}
function LinkButton(props: LinkButtonProps) {
    return (
        <Link href={props.url} className="bg-purple-500 hover:bg-purple-700 rounded xl:w-64 lg:w-42 w-full font-bold mx-3
            h-24 flex justify-center text-bold text-white text-lg items-center">{props.text}
        </Link>
    );
}
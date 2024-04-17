import {StudySetProps, StudySetPropsListProps} from "@/ui/data/data";
import Link from "next/link";
import {formatDate} from "@/ui/utils/format";


export function StudySet({ props}: StudySetProps) {
    let link = `/sets/${ props.id }`
    return (
    <Link href = {link} className="w-1/2 p-4 m-4 rounded-md border border-gray-400">
        <div className="flex flex-col w-full items-start">
            <h3 className="text-2xl font-bold">
                {props.name}
            </h3>
            <p className="text-gray-500">{props.size} terms</p>
        </div>
        <div className="flex flex-col w-full items-end">
            <p className="text-gray-500 mr-4">Created: {formatDate(props.created_at)}</p>
        </div>
    </Link>
    )
}

export function StudSetList({props}: StudySetPropsListProps) {

    return (
        <div className="flex border flex-col items-center">
            {props.list.map((set, index) => (
                <StudySet key={index} props={set} />
            ))}
        </div>);
    }


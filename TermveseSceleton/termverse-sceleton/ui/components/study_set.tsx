import {StudySetCombined, StudySetCombinedProps, StudySetProps, StudySetResponseListProps} from "@/ui/data/data";
import Link from "next/link";

export function StudySet({ props}: StudySetProps) {
    let link = `/sets/${ props.id }`
    return (
    <Link href = {link} className="w-1/2 p-4 m-4 rounded-md border border-gray-400">
        <div className="flex flex-col w-full items-start">
            <h3 className="text-2xl font-bold">
                {props.name}
            </h3>
            <p className="text-gray-500">Set of terms</p>
        </div>
        <div className="flex flex-col w-full items-end">
            <p className="text-gray-500 mr-4">Created: {props.createdAt}</p>
        </div>
    </Link>
    )
}

export function StudSetList({props}: StudySetResponseListProps) {
    if (!props.list) {
        return (<div>No study sets...</div>)
    }
    return props.list.length > 0 ?
        (<div className="flex border w-full flex-col items-center">
            {props.list.map((set, index) => (
                <StudySet key={index} props={set} />
            ))}
        </div>)
         : (<p className="flex border w-full flex-col items-center"> User has no study sets</p>)
}


export function StudySetWithOwner({ props }: StudySetCombined) {
    let link = `/sets/${ props.id }`
    return (
        <div className="w-3/4 p-4 m-4 rounded-md border border-gray-400">
            <div className="flex flex-col w-full items-start">
                <h3 className="text-2xl font-bold">
                    <Link href= {link}>
                        {props.name}
                    </Link>
                </h3>
                { props.owner ?
                    (<Link href={`users/${ props.owner.id }`} className="text-gray-500"> Study set by {props.owner.name} </Link>) :
                    (<p className="text-gray-500">Set of terms</p>)
                }
            </div>
            <div className="flex flex-col w-full items-end">
                <p className="text-gray-500 mr-4">Created: {props.createdAt}</p>
            </div>
        </div>
    )
}

export function StudSetListCombined({props}: StudySetCombinedProps) {
    if (!props.list) {
        return (<div>No study sets...</div>)
    }
    return (
        <div className="flex border w-full flex-col items-center">
            {props.list.map((set, index) => (
                <StudySetWithOwner key={index} props={ set } />
            ))}
        </div>);
}


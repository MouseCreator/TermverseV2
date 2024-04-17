import {UserProps} from "@/ui/data/data";
import Link from "next/link";

export function User({ props}: UserProps) {
    let link = `/users/${ props.id }`
    return (
        <Link href = {link} className="w-1/2 p-4 m-2 rounded-md border border-gray-400">
            <div className="flex flex-col w-full items-start">
                <h3 className="text-2xl font-bold">
                    {props.name}
                </h3>
            </div>
        </Link>
    )
}
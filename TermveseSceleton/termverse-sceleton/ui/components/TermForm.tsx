import Image from "next/image";

export default function TermForm({ id, onDelete, bg }: { id: string; onDelete: (id: string) => void, bg: string }) {
    return (
        <div className="border-2 border-gray-200 px-10 py-3 rounded-2xl my-2 w-[32vw]"
        style={{
            background: bg
        }}>
            <div className="flex items-center space-x-4">
                <input className="flex-grow p-2 border rounded" placeholder="Term" />
                <input className="flex-grow p-2 border rounded" placeholder="Definition" />
                <button onClick={() => onDelete(id)} className="w-8 h-8">
                    <Image src="/trash.svg" alt="Delete" width="32" height="32" />
                </button>
            </div>
        </div>
    );
}
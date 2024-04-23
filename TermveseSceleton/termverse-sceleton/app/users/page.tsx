
import {User} from "@/ui/components/user";

export default function Page() {
    return (
        <main className="w-full">
            <div className="flex border flex-col items-center">
                <User props={{id: 1, name: "User 1", profile_picture_url: null}} />
                <User props={{id: 2, name: "User 2", profile_picture_url: null}} />
                <User props={{id: 3, name: "User 3", profile_picture_url: null}} />
            </div>
        </main>
    )
}
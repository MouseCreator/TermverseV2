import {User} from "@/app/ui/data/User";
import Image from "next/image";
import {ProfilePictureRound} from "@/app/ui/profile/profilePictureMid";
interface UserDataProps {
    user: User;
}
export function UserHead( { user } : UserDataProps) {

    return (
        <div className="flex justify-center text-center flex-col w-full container py-8">
            <UserTop user={user}/>
            <h1 className="font-bold text-3xl">{user.name}</h1>
        </div>
    );
}

export function UserTop({user} : UserDataProps) {
    return (
        <div>
            <div className="flex w-full rounded h-64 bg-purple-500 justify-center items-end">
                <div className="rounded-full bg-white w-40 h-40"
                     style={
                         {
                             marginBottom: -80,
                         }
                     }>
                    <ProfilePictureRound user={user}/>
                </div>
            </div>
            <div className="h-24"/>
        </div>
    );
}

export function getUserData(id: number): User {
    const users: { [id: number]: User; } = {};
    users[456] = {
        id: 456,
        name: 'Tails123',
        profile_picture_url: '/mock/Tails.png',
    }
    return users[id];
}
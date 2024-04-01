import {AuthorProfile, HeadData, StudySetTitle} from "@/app/ui/studysets/headdata";
import {User} from "@/app/ui/data/User";
interface UserDataProps {
    user: User;
}
export function UserData( { user } : UserDataProps) {

    return (
        <div className="container mx-auto py-8">

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
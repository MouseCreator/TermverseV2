import {User } from '@/app/ui/data/User'
import ProfilePictureMid from "@/app/ui/profile/profilePictureMid";
import {notFound} from "next/navigation";
import {getUserData} from "@/app/ui/users/userhead";

export interface HeadDataI {
    id: number;
    name: string;
    picture_url: string | null;
    description: string;
    author: User;
    created_at: Date;
}

export interface HeadDataProps {
    headData: HeadDataI;
}

export function HeadData( { headData } : HeadDataProps) {

    return (
        <div className="container mx-auto">
            <StudySetTitle name={headData.name} picture_url={headData.picture_url}/>
            <div className="flex justify-between">
                <div className="pl-8">
                    <AuthorProfile author={headData.author}/>
                    <p className="text-gray-500">{formatDate(headData.created_at)}</p>
                </div>

                <div className="pt-8 pr-8">
                    <SaveBtn/>
                    <EditBtn/>
                </div>
            </div>
            <Description headData={headData}/>
        </div>
    );
}

export function SaveBtn() {
    return (
        <button className="bg-purple-500 text-white px-4 py-2 rounded font-bold w-32 h-12 mx-4">
            Save
        </button>
    )
}

export function EditBtn() {
    return (
        <button className="bg-purple-500 text-white px-4 py-2 rounded font-bold w-32 h-12 mx-4">
            Edit
        </button>
    )
}

function formatDate(date: Date): String {
    return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: '2-digit'
    });
}
interface AuthorProfileProps {
    author: User;
}
export function AuthorProfile(props : AuthorProfileProps) {
    return <div className="flex items-center mt-4">
        <ProfilePictureMid user={props.author}/>
        <h3 className="ml-2 mb-2 font-bold">{props.author.name}</h3>
        <p></p>
    </div>;
}
interface StudySetTitleProps {
    picture_url: string | null,
    name: string
}

export function StudySetTitle(props : StudySetTitleProps) {
    return (
        <div
            className="w-full h-[300px] bg-purple-500 rounded-lg relative overflow-hidden"
        >
            <div
                className="absolute inset-0 bg-cover bg-center"
                style={{
                    backgroundImage: props.picture_url ? `url(${props.picture_url})` : 'none',
                    backgroundRepeat: 'no-repeat',
                    backgroundPosition: 'center',
                    margin: '0 auto',
                }}
            ></div>
            <h1 className="text-4xl font-bold text-white absolute bottom-8 left-4">{props.name}</h1>
        </div>
    );
}
export async function getStudySetHeadFromServer(setId: number): Promise<HeadDataProps>  {
    console.log('Fetching Study Set Data...');
    const sets: { [id: number]: HeadDataI; } = {};
    sets[123] = {
        id: 123,
        name: 'English Words',
        picture_url: '/mock/BG1.png',
        description: 'My first study set!',
        created_at: new Date(Date.now()),
        author: getUserData(456)
    };
    let headData = sets[setId];
    if (!headData) {
        notFound();
    }
    return {
        headData
    };
}


export function Description({headData}: HeadDataProps) {
    return (
        <div className="mt-4 p-6 pl-8 min-h-32 w-full rounded bg-gray-300">
            <h3 className="font-bold text-lg">Description:</h3>
            <p>{headData.description}</p>
        </div>
    )
}
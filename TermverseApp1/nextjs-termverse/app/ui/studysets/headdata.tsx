import {User } from '@/app/ui/data/User'
import ProfilePictureMid from "@/app/ui/profile/profilePictureMid";
import {notFound} from "next/navigation";

interface HeadData {
    id: number;
    name: string;
    picture_url: string | null;
    description: string;
    author: User;
}

interface HeadDataProps {
    headData: HeadData;
}

export function HeadData( { headData } : HeadDataProps) {
    return (
        <div className="container mx-auto py-8">
            <StudySetTitle name={headData.name} picture_url={headData.picture_url}/>
            <div className="flex items-center mt-4">
                <ProfilePictureMid user={headData.author}/>
                <h3 className="mb-4 font-bold">{headData.author.name}</h3>
            </div>
        </div>
    );
}
interface StudySetTitleProps {
    picture_url: string | null,
    name: string
}

export function StudySetTitle(props : StudySetTitleProps) {
    return (
        <div
            className="w-full h-[300px] bg-purple-500 rounded-lg relative overflow-hidden"
            style={{
                backgroundImage: props.picture_url ? `url(${props.picture_url})` : 'none',
            }}
        >
            <div
                className="absolute inset-0"
                style={{
                    backgroundImage: props.picture_url ? `url(${props.picture_url})` : 'none',
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                }}
            ></div>
            <h1 className="text-3xl font-bold text-white absolute bottom-4 left-4">{props.name}</h1>
        </div>
    );
}
export async function getServerSideProps(setId: number): Promise<HeadDataProps>  {
    console.log('Fetching Study Set Data...');
    const sets: { [id: number]: HeadData; } = {};
    sets[123] = {
        id: 123,
        name: 'English Words',
        picture_url: '/mock/BG1.png',
        description: 'My first study set!',
        author: {
            id: 456,
            name: 'Tails123',
            profile_picture_url: '/mock/Tails.png',
        },
    };
    let headData = sets[setId];
    if (!headData) {
        notFound();
    }
    return {
        headData
    };
}
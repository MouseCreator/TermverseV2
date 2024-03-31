import {User } from '@/app/ui/data/User'
import ProfilePictureMid from "@/app/ui/profile/profilePictureMid";

interface HeadData {
    id: number;
    name: string;
    picture_url: string;
    description: string;
    author: User;
}

interface HeadDataProps {
    headData: HeadData;
}

export function HeadData( { headData } : HeadDataProps) {
    return (
        <div className="container mx-auto py-8">
            <div className = "bg-purple-500 text-white w-full h-[300px] bg-purple-500 rounded-lg relative">
                <h1 className="text-3xl font-bold text-white absolute bottom-4 left-4">{headData.name}</h1>
            </div>
            <div className="flex items-center mt-4">
                <ProfilePictureMid user={headData.author}/>
                <h3 className="mb-4 font-bold">{headData.author.name}</h3>
            </div>
        </div>
    );
}

export async function getServerSideProps(setId: number): Promise<HeadDataProps>  {
    console.log('Fetching Study Set Data...');
    const sets: { [id: number]: HeadData; } = {};
    sets[1] = {
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
    return {
        headData
    };
}
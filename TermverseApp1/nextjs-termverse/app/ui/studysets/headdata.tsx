import Image from 'next/image';

interface Author {
    id: number;
    name: string;
    profile_picture_url: string;
}

interface HeadData {
    id: number;
    name: string;
    description: string;
    author: Author;
}

interface HeadDataProps {
    headData: HeadData;
}

export function HeadData( { headData } : HeadDataProps) {
    return (
        <div className="container mx-auto py-8">
            <h1 className="text-3xl font-bold mb-4">Head Data</h1>
            <table className="table-auto border-collapse border border-gray-300">
                <thead>
                <tr>
                    <th className="px-4 py-2 border border-gray-300">ID</th>
                    <th className="px-4 py-2 border border-gray-300">Name</th>
                    <th className="px-4 py-2 border border-gray-300">Description</th>
                    <th className="px-4 py-2 border border-gray-300">Author</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td className="px-4 py-2 border border-gray-300">{headData.id}</td>
                    <td className="px-4 py-2 border border-gray-300">{headData.name}</td>
                    <td className="px-4 py-2 border border-gray-300">{headData.description}</td>
                    <td className="px-4 py-2 border border-gray-300">
                        <div className="flex items-center">
                            <div className="w-10 h-10 rounded-full overflow-hidden mr-2">
                                <Image
                                    src={headData.author.profile_picture_url}
                                    alt={headData.author.name}
                                    width={40}
                                    height={40}
                                    objectFit="cover"
                                />
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    );
}

export async function getServerSideProps(): Promise<HeadDataProps>  {
    console.log('Fetching Study Set Data...');
    const headData: HeadData = {
        id: 123,
        name: 'Some Name',
        description: 'Some Description',
        author: {
            id: 456,
            name: 'Author\'s Name',
            profile_picture_url: '/mock/Tails.png',
        },
    };

    return {
        headData
    };
}
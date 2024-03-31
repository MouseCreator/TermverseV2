import {User} from "@/app/ui/data/User";
import Image from 'next/image';
import Link from 'next/link';
interface ProfilePictureMidProps {
    user: User;
}

export default function ProfilePictureMid({user}: ProfilePictureMidProps) {
    return (
        <Link href={`/users/${user.id}`}>
            <div className="relative inline-block">
                <div className="w-12 h-12 rounded-full overflow-hidden">
                    <Image
                        src={user.profile_picture_url}
                        alt={user.name}
                        width={48}
                        height={48}
                        objectFit="cover"
                    />
                </div>
            </div>
        </Link>
    );
};

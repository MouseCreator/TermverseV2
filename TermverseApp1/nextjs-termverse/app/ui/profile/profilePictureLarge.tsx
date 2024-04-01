import {User} from "@/app/ui/data/User";
import Link from "next/link";
import Image from "next/image";

interface ProfilePictureLargeProps {
    user: User;
}

export default function ProfilePictureMid({user}: ProfilePictureLargeProps) {
    return (
        <Link href={`/users/${user.id}`}>
            <div className="relative inline-block">
                <div className="w-20 h-20 rounded-full overflow-hidden">
                    <Image
                        src={user.profile_picture_url}
                        alt={user.name}
                        width={80}
                        height={80}
                        objectFit="cover"
                    />
                </div>
            </div>
        </Link>
    );
};
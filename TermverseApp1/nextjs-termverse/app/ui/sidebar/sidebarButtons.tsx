import Link from 'next/link';

 export function SidebarButtons() {
    return (
        <nav>
            <ul className="space-y-2">
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Home</div>
                    </Link>
                </li>
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Profile</div>
                    </Link>
                </li>
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Study</div>
                    </Link>
                </li>
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Explore</div>
                    </Link>
                </li>
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">About</div>
                    </Link>
                </li>
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Help</div>
                    </Link>
                </li>
            </ul>
        </nav>
    );
}
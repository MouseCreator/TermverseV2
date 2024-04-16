import Image from "next/image";
import Link from "next/link";
export function Header() {
    return (
        <header className="flex flex-col items-center">
            <HeaderLogo />
            <HeaderLinks />
        </header>
    );
}

function HeaderLogo() {
    return (
        <div className="container mx-auto mb-4">
            <div className="flex flex-col ml-4 justify-center items-center sm:flex-row">
                <div className="mb-4 sm:mr-4 sm:mb-0">
                    <Image src="/app-logo.png" alt="App Logo" width={60} height={60} />
                </div>
                <h1 className="text-purple-600 text-3xl font-bold text-center sm:text-left">Termverse</h1>
            </div>
        </div>
    )
}

function HeaderLinks() {
    return(
        <nav>
            <ul className="space-x-2 flex flex-row">
                <li>
                    <Link href="/">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Home</div>
                    </Link>
                </li>
                <li>
                    <Link href="/users">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Users</div>
                    </Link>
                </li>
                <li>
                    <Link href="/sets">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Study Sets</div>
                    </Link>
                </li>
                <li>
                    <Link href="/profile">
                        <div className="block px-4 py-2 rounded-md hover:bg-purple-500 hover:text-white">Profile</div>
                    </Link>
                </li>
            </ul>
        </nav>
    )
}
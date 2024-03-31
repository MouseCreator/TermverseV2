import Image from 'next/image';
export function Header() {
    return (
        <header className="bg-purple-500 py-4">
            <div className="container mx-auto flex items-center">
                <div className="mr-4">
                    <Image src="/app-logo.png" alt="App Logo" width={60} height={60} />
                </div>
                <h1 className="text-white text-3xl font-bold">Termverse</h1>
            </div>
        </header>
    )
}
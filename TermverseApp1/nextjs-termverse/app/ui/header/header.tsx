import Image from 'next/image';
export function Header() {
    return (
        <header className="bg-purple-500 py-4">
            <div className="container mx-auto">
                <div className=" flex flex-col ml-4 sm:justify-start items-center sm:flex-row">
                    <div className="mb-4 sm:mr-4 sm:mb-0">
                        <Image src="/app-logo.png" alt="App Logo" width={60} height={60} />
                    </div>
                    <h1 className="text-white text-3xl font-bold text-center sm:text-left">Termverse</h1>
                </div>
            </div>
        </header>
    );
}

export function TermVerseLogo() {
    return (
        <div className="flex sm:justify-start items-center p-4">
            <div className="mb-4 sm:mr-4 sm:mb-0">
                <Image src="/app-logo.png" alt="App Logo" width={60} height={60} />
            </div>
            <h1 className="text-white text-3xl font-bold text-center sm:text-left">Termverse</h1>
        </div>
    );
}
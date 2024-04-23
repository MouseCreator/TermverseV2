import {Header} from "@/ui/header/header";

export default function SetsLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="min-h-screen flex flex-col items-center p-12">
            <Header />
            {children}
        </div>
    );
}
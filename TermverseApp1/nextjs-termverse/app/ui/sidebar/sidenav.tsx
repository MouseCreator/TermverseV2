import {TermVerseLogo} from "@/app/ui/header/header";
import {SidebarButtons} from "@/app/ui/sidebar/sidebarButtons";

export function Sidenav() {
    return (
        <aside className="bg-gray-200 min-h-screen">
            <div className="bg-purple-500 mb-4">
                <TermVerseLogo />
            </div>
            <div className="bg-gray-200 w-64 min-h-screen p-4">
                <SidebarButtons />
            </div>
        </aside>
    );
}
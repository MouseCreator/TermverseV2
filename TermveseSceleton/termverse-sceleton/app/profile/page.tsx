'use client'

import {useActive, useActiveOrRedirect} from "@/app/utils/isActive";
export default function Page() {
    let state = useActive();
    if (state === 'loading') {
        return(<div>Loading...</div>);
    }
    if (state === 'inactive') {
        return <div>Not active!</div>
    }
    if (state === 'active') {
        return <div>Active!</div>
    }
    return (
        <div>
            <h1>Profile Page</h1>
            {/* Profile content */}
        </div>
    );
};
'use client'
import { useRouter } from "next/navigation";

export default function ComponentName() {
    const router = useRouter();
    router.push('/signin')

    return (<div>Hello!</div>);
}
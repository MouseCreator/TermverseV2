import Link from "next/link";

export function Signup() {
    return (
        <div className="flex justify-center flex-col h-[90svh]">
            <main className="w-full flex justify-center items-center">
                <div className="bg-gray-100 rounded-2xl p-4">
                <div className="flex flex-col items-center m-8">
                    <h2 className="text-3xl pb-8">Sign up</h2>
                    <form>
                        <div className="flex flex-col items-center pb-4">
                            <label>Login</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="login"/>
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="password"/>
                        </div>
                        <div className="flex flex-col items-center pb-4">
                            <label>Confirm password</label>
                            <input className="border-2 border-gray-400 p-1" autoComplete="off" type="text" name="confirm-password"/>
                        </div>
                        <div className="pb-4">
                            <button type="submit"
                                    className="bg-purple-600 rounded text-white w-full h-8 hover:bg-purple-400">
                                Submit
                            </button>
                        </div>
                    </form>
                </div>
                <nav className="w-full flex justify-between flex-row px-2">
                    <Link href="/signin" className="text-blue-600">
                        Sign in
                    </Link>
                    <Link href="/" className="text-blue-600">
                        Home
                    </Link>
                </nav>
                </div>
            </main>
        </div>
    )
}
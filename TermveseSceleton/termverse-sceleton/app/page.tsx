import {Header} from "@/ui/header/header";

export default function Home() {
  return (
      <div>
        <main className="min-h-screen flex flex-col items-center p-12">
            <Header />
            <h1 className="text-2xl">Welcome to Termverse!</h1>
        </main>
      </div>
  );
}

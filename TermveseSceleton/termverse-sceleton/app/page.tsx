import {Header} from "@/ui/header/header";

export default function Home() {
  return (
      <div>
        <main className="flex min-h-screen flex-col items-center justify-between p-12">
            <Header />
            <h1 className="text-red-600">Hello, world</h1>
        </main>
      </div>
  );
}

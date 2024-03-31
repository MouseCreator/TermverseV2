import { Header } from '@/app/ui/header/header'
import { Sidebar } from '@/app/ui/sidebar/sidebar'


export default function Home() {
  return (
      <div>
          <Header />
        <div className="flex">
          <Sidebar />
          <main className="flex min-h-screen flex-col items-center justify-between p-24">
            <div>Hello, world!</div>
          </main>
        </div>

      </div>
  );
}

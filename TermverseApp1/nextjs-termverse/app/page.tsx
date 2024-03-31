import { Header } from '@/app/ui/header/header'
import { SidebarButtons } from '@/app/ui/sidebar/sidebarButtons'


export default function Home() {
  return (
      <div>
          <Header />
        <div className="flex">
          <SidebarButtons />
          <main className="flex min-h-screen flex-col items-center justify-between p-24">
            <div>Hello, world!</div>
          </main>
        </div>

      </div>
  );
}

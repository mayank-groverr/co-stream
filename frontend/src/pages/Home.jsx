import { useState } from "react";
import SideBar from "../components/sidebar/SideBar";
import ChatRoom from "../components/chat_space/ChatSpace";





const Home = () => {
  const [chatOpen, setChatOpen] = useState(false);
  return (
    <div className="flex h-dvh w-full bg-black text-white">
      <SideBar chatOpen={chatOpen} onRoomSelect={() => setChatOpen(true)} />

      {/* Phone: only visible when a room is open. Laptop: always visible */}
      <main className={`${chatOpen ? 'flex' : 'hidden'} md:flex min-w-0 flex-1`}>
        <ChatRoom onBack={() => setChatOpen(false)} />
      </main>
    </div>
  );
};

export default Home;
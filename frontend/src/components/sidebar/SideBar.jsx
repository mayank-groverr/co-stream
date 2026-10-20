import profilePic from '../../assets/profile.png'
import Room from '../room_display_card/Room'
import { useState } from 'react'
import ButtonDisplayCard from '../button_display_card/ButtonDisplayCard'
import { PROTECTED_ENDPOINTS } from '../../api/UrlPath'

const SideBar = ({ chatOpen, onRoomSelect }) => {
    const [showJoinRoomCard, setShowJoinRoomCard] = useState(false);
    const [showCreateRoomCard, setShowCreateRoomCard] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false); // 3-dot menu

    const handleJoinRoomSubmit = async (roomId) => {
        setShowJoinRoomCard(false);
        try {
            const response = await fetch(PROTECTED_ENDPOINTS.JOIN_ROOM(roomId), {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
            });
            const data = await response.json();
            // use data here
        } catch (err) {
            console.error("Failed to join room:", err);
        }
    };

    const handleCreateRoom = (roomName) => {
        setShowCreateRoomCard(false);
        // Handle create room logic
    };

    return (
        <aside
            className={`${chatOpen ? 'hidden md:flex' : 'flex'}
                h-full w-full flex-col gap-2 border-r border-gray-700 bg-black p-4 text-white
                md:w-[32%] md:min-w-[300px] md:max-w-[420px] md:shrink-0 md:p-6`}
        >
            {/* Title + 3-dot menu (all sizes) */}
            <div className="relative z-20 flex items-center justify-between">
                <h2 className="text-xl font-bold text-white md:text-2xl">Co-stream</h2>

                <button
                    type="button"
                    onClick={() => setMenuOpen((open) => !open)}
                    aria-label="More actions"
                    aria-expanded={menuOpen}
                    className="rounded p-1.5 text-white hover:bg-gray-800"
                >
                    <svg viewBox="0 0 24 24" fill="currentColor" className="h-6 w-6 text-white">
                        <circle cx="12" cy="5" r="2" />
                        <circle cx="12" cy="12" r="2" />
                        <circle cx="12" cy="19" r="2" />
                    </svg>
                </button>

                {menuOpen && (
                    <>
                        {/* Invisible layer: tap outside to close */}
                        <div
                            onClick={() => setMenuOpen(false)}
                            className="fixed inset-0 z-10"
                        />

                        <div
                            className="absolute right-0 top-full z-20 mt-2 w-48 rounded-lg border border-gray-500
                                       bg-[#2a2a2a] py-1 text-white shadow-lg shadow-black/50
                                       [&_button]:block [&_button]:w-full [&_button]:px-4 [&_button]:py-2.5
                                       [&_button]:text-left [&_button]:text-sm [&_button]:text-white
                                       [&_button]:hover:bg-gray-600"
                        >
                            <button
                                type="button"
                                onClick={() => { setMenuOpen(false); setShowJoinRoomCard(true); }}
                            >
                                Join Room
                            </button>
                            <button
                                type="button"
                                onClick={() => { setMenuOpen(false); setShowCreateRoomCard(true); }}
                            >
                                Create Room
                            </button>
                        </div>
                    </>
                )}
            </div>

            {showJoinRoomCard && (
                <ButtonDisplayCard
                    title="Join Room"
                    placeholderValue="Room ID"
                    errorText="Please enter a room ID."
                    buttonText="Join"
                    onSubmit={handleJoinRoomSubmit}
                    onCancel={() => setShowJoinRoomCard(false)}
                />
            )}

            {showCreateRoomCard && (
                <ButtonDisplayCard
                    title="Create Room"
                    placeholderValue="Room Name"
                    errorText="Please enter a room name."
                    buttonText="Create"
                    onSubmit={handleCreateRoom}
                    onCancel={() => setShowCreateRoomCard(false)}
                />
            )}

            {/* Room list: scrolls inside its own box */}
            <div
                id="room-list-section"
                className="flex min-h-0 flex-1 flex-col gap-2 overflow-y-auto rounded-lg border border-gray-700 p-3 md:p-4"
            >
                {/* Clicking a room opens the chat on phone */}
                <div onClick={onRoomSelect} className="cursor-pointer">
                    <Room />
                </div>
            </div>
            
        </aside>
    )
}

export default SideBar
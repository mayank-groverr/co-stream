import profilePic from '../../assets/profile.png';
import copyIcon from '../../assets/copy_to_clipboard.png';
import audioCall from '../../assets/audio_call.png';
import videoCall from '../../assets/video_call.png';
import exitGroup from '../../assets/exit_icon.png';
import chatWallpaper from '../../assets/chat_wallpaper.png';

const ChatRoom = ({ onBack }) => {
  return (
    <div
      style={{ backgroundImage: `url(${chatWallpaper})` }}
      className="flex w-full h-full min-h-0 flex-col bg-cover bg-center text-white"
    >

      {/* Header (relative z-20 so the dropdown sits above the chat area) */}
      <div
        id="room-details"
        className="relative z-20 shrink-0 min-h-14 sm:min-h-16 md:h-18 flex flex-row items-center gap-2 bg-black
                   border-b border-gray-700 px-2 sm:px-6 md:px-10 py-2"
      >
        {/* Back arrow: phone only */}
        <button
          type="button"
          onClick={onBack}
          aria-label="Back to rooms"
          className="md:hidden -ml-1 shrink-0 rounded p-1.5 text-white hover:bg-gray-800"
        >
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
            className="h-6 w-6"
          >
            <path d="M15 5l-7 7 7 7" />
          </svg>
        </button>

        <div
          id="room-profile-picture"
          style={{ backgroundImage: `url(${profilePic})` }}
          className="h-9 w-9 sm:h-10 sm:w-10 md:h-12 md:w-12 shrink-0 bg-cover bg-center bg-no-repeat"
        ></div>

        {/* min-w-0 lets the text truncate instead of pushing the icons off-screen */}
        <div id="room-name" className="flex flex-col ml-1 sm:ml-2 min-w-0">
          <p className="text-base md:text-xl font-semibold truncate">Room Name</p>
          <p className="text-gray-500 text-xs sm:text-sm truncate">Total Members</p>
        </div>

        {/* Icons inline: sm and up */}
        <div
          id="room-actions"
          className="ml-auto shrink-0 hidden sm:flex flex-row items-center gap-3 md:gap-4
                     [&_span]:shrink-0 sm:[&_span]:h-10 sm:[&_span]:w-10 md:[&_span]:h-12 md:[&_span]:w-12
                     [&_span]:bg-contain [&_span]:bg-no-repeat [&_span]:bg-center [&_span]:cursor-pointer"
        >
          <span style={{ backgroundImage: `url(${audioCall})` }}></span>
          <span style={{ backgroundImage: `url(${videoCall})` }}></span>
          <span style={{ backgroundImage: `url(${copyIcon})` }}></span>
          <span style={{ backgroundImage: `url(${exitGroup})` }}></span>
        </div>

        {/* 3-dot dropdown: phone only */}
        <details id="room-actions-menu" className="relative ml-auto shrink-0 sm:hidden">
          <summary
            aria-label="More actions"
            className="list-none cursor-pointer rounded p-1.5 hover:bg-gray-800
                       [&::-webkit-details-marker]:hidden"
          >
            <svg viewBox="0 0 24 24" fill="currentColor" className="h-6 w-6">
              <circle cx="12" cy="5" r="2" />
              <circle cx="12" cy="12" r="2" />
              <circle cx="12" cy="19" r="2" />
            </svg>
          </summary>

          <div
            className="absolute right-0 top-full mt-2 w-52 rounded-lg border border-gray-700
                       bg-[#1f1f1f] py-1 shadow-lg
                       [&_button]:flex [&_button]:w-full [&_button]:items-center [&_button]:gap-3
                       [&_button]:px-4 [&_button]:py-2.5 [&_button]:text-left [&_button]:text-sm
                       [&_button]:hover:bg-gray-700
                       [&_span]:h-6 [&_span]:w-6 [&_span]:shrink-0
                       [&_span]:bg-contain [&_span]:bg-no-repeat [&_span]:bg-center"
          >
            <button type="button">
              <span style={{ backgroundImage: `url(${audioCall})` }}></span>
              Audio call
            </button>
            <button type="button">
              <span style={{ backgroundImage: `url(${videoCall})` }}></span>
              Video call
            </button>
            <button type="button">
              <span style={{ backgroundImage: `url(${copyIcon})` }}></span>
              Copy room ID
            </button>
            <button type="button">
              <span style={{ backgroundImage: `url(${exitGroup})` }}></span>
              Exit group
            </button>
          </div>
        </details>
      </div>

      {/* Spacer so the input stays pinned to the bottom (messages will go here later) */}
      <div className="flex-1 min-h-0 overflow-y-auto"></div>

      {/* Message input bar */}
      <div
        id="send-message"
        className="min-h-14 sm:min-h-16 shrink-0 border-t border-gray-700 bg-black
                   flex flex-row items-end gap-2 px-2 sm:px-3 py-2"
      >
        <textarea
          rows={1}
          placeholder="Type a message..."
          className="peer flex-1 min-w-0 min-h-10 sm:min-h-11 max-h-32 resize-none
                     [field-sizing:content] overflow-y-auto
                     rounded-lg bg-[#1f1f1f] border border-[#1f1f1f]
                     text-white text-base placeholder:text-gray-500
                     px-3 sm:px-4 py-2 sm:py-2.5 outline-none"
        ></textarea>

        <div
          id="send-button"
          role="button"
          aria-label="Send message"
          className="h-9 w-9 sm:h-10 sm:w-10 shrink-0 rounded-full
                     bg-green-500 hover:bg-green-600
                     peer-placeholder-shown:bg-gray-600 peer-placeholder-shown:hover:bg-gray-600
                     peer-placeholder-shown:cursor-not-allowed
                     text-white text-lg sm:text-xl flex items-center justify-center
                     cursor-pointer select-none"
        >
          &#10148;
        </div>
      </div>

    </div>
  );
};

export default ChatRoom;
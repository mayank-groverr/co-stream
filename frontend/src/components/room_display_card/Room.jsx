import profilePic from '../../assets/profile.png'

const Room = () => {
    return (
        <div className="flex items-center gap-2 p-2 border rounded-lg border-gray-300">

            <span
                style={{ backgroundImage: `url(${profilePic})` }}
                className="flex flex-col items-center gap-2 w-8 h-8 rounded-full bg-cover bg-center bg-no-repeat"
            />

            <div id = "room-info" className="flex flex-col">

                <p className="text-sm md:text-base font-semibold">Room Name</p>
                <p className="text-xs md:text-sm text-gray-500">Members</p>
            </div>

        </div>
    )
}

export default Room
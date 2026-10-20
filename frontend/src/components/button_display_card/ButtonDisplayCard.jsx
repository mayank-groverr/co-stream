import { useRef, useState } from "react";

const ButtonDisplayCard = (props) => {
    const inputRef = useRef(null);
    const [error, setError] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        const trimmed = inputRef.current.value.trim();
        if (!trimmed) {
            setError("Input cannot be empty.");
            return;
        }
        props.onSubmit?.(trimmed);
    };

    return (
        // Full-screen overlay that centres the card on the page
        <div
            className="fixed inset-0 z-[100] flex items-center justify-center bg-black/70 p-4"
            onClick={props.onCancel}
        >
            <form
                onSubmit={handleSubmit}
                onClick={(e) => e.stopPropagation()}
                className="flex w-full max-w-sm flex-col gap-4 rounded-lg border border-gray-600
                           bg-black p-6 text-white shadow-2xl
                           [&_input]:rounded [&_input]:border [&_input]:border-gray-600
                           [&_input]:bg-[#1f1f1f] [&_input]:px-3 [&_input]:py-2
                           [&_input]:text-white [&_input]:placeholder:text-gray-500
                           [&_input]:outline-none [&_input]:focus:border-blue-500"
            >
                <p className="text-center font-semibold">{props.title || "Enter a value"}</p>
                {error && <p className="text-center text-base font-bold text-red-500">{error}</p>}

                <input
                    ref={inputRef}
                    type="text"
                    autoFocus
                    placeholder={props.placeholderValue || "Enter value..."}
                />

                <div
                    id="action-buttons"
                    className="flex flex-row gap-3 sm:gap-4
                    [&_button]:flex-1
                    [&_button]:text-white
                    [&_button]:py-1
                    [&_button]:px-2"
                >
                    <button
                        type="submit"
                        className="rounded border border-blue-500 bg-blue-500 hover:bg-blue-700"
                    >
                        {props.buttonText || "Submit"}
                    </button>
                    <button
                        type="button"
                        onClick={props.onCancel}
                        className="rounded border border-red-500 bg-red-500 hover:bg-red-700"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ButtonDisplayCard;
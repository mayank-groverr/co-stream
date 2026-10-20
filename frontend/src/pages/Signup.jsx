import { Link, useNavigate } from 'react-router-dom'
import { PUBLIC_ENDPOINTS } from '../api/UrlPath.js'
import React, { useState } from 'react'

const Signup = () => {

  const SignupUrl = PUBLIC_ENDPOINTS.SIGNUP;
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    const data = Object.fromEntries(new FormData(e.target));

    try {
      const response = await fetch(SignupUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });

      const responseJson = await response.json();

      if (!response.ok) {
        if (response.status === 400) {
          setError('Please fill in all required fields.');
        }
        return;
      }

      navigate('/login', {
        state: {email: responseJson.email},
      });
    } catch (err) {
      setError('Something went wrong. Please try again.');
    }
  };



  return (
    <div className="flex flex-col items-center justify-center min-h-screen gap-6 px-4">
  <p className="text-center text-2xl font-bold">Create an Account</p>

  <form onSubmit={handleSubmit}
    className="flex flex-col gap-4 w-full max-w-sm p-6 border rounded-lg
    [&_input]:border
    [&_input]:rounded
    [&_input]:px-3
    [&_input]:py-2
    [&_input]:focus:outline-none
    [&_input]:focus:ring-2
    [&_input]:focus:ring-blue-500"
  >

    {error && <p className="text-red-500 text-center">{error}</p>}

    <label htmlFor="name">Name</label>
    <input id="name" type="text" name="name" placeholder="Your name" required />

    <label htmlFor="email">Email</label>
    <input id="email" type="email" name="email" placeholder="you@example.com" required />

    <label htmlFor="password">Password</label>
    <input id="password" type="password" name="password" placeholder="••••••••" required />

    <button
      type="submit"
      className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
    >
      Sign Up
    </button>
  </form>

  <Link to="/login">
    Already have an account? <span className="text-blue-500 hover:underline">Login</span>
  </Link>
</div>
  )
}

export default Signup
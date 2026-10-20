import { Routes, Route, Navigate  } from 'react-router-dom'
import Signup from './pages/Signup'
import Login from './pages/Login'

import Home from './pages/Home'

function App() {


  return (

    // If jwt present redirect to home else redirect to login (to be implemented)
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  )
}

export default App

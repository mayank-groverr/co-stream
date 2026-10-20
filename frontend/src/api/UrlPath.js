const SERVER_URL = import.meta.env.VITE_API_URL
const PUBLIC_API_BASE = "api/public/v1"

export const PUBLIC_ENDPOINTS = {
  LOGIN: `${SERVER_URL}/${PUBLIC_API_BASE}/login`,
  SIGNUP: `${SERVER_URL}/${PUBLIC_API_BASE}/signup`,
}

export const PROTECTED_ENDPOINTS = {
  JOIN_ROOM: '${SERVER_URL}/${PUBLIC_API_BASE}/rooms/${encodeURIComponent(roomId)}/join',
  CREATE_ROOM: '${SERVER_URL}/${PUBLIC_API_BASE}/rooms/create',
}
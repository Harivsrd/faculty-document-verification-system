import axios from 'axios'

<<<<<<< HEAD
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api'
=======
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

const api = axios.create({
  baseURL: API_BASE_URL,
})

<<<<<<< HEAD
// Attach the JWT to every outgoing request.
=======
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('fcs_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

<<<<<<< HEAD
// On a 401, clear the stale session and bounce to login.
=======
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('fcs_token')
      localStorage.removeItem('fcs_user')
<<<<<<< HEAD
      if (window.location.pathname !== '/login') {
=======
      if (!window.location.pathname.startsWith('/login')) {
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default api

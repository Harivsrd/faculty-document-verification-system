import api from './api'

export const authService = {
<<<<<<< HEAD
  register: (payload) => api.post('/auth/register', payload).then((res) => res.data),
  login: (payload) => api.post('/auth/login', payload).then((res) => res.data),
=======
  register: (data) => api.post('/auth/register', data).then((res) => res.data),
  login: (data) => api.post('/auth/login', data).then((res) => res.data),
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}

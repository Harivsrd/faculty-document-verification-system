import api from './api'

export const profileService = {
  getMyProfile: () => api.get('/faculty/profile').then((res) => res.data),
  updateMyProfile: (payload) => api.put('/faculty/profile', payload).then((res) => res.data),
  getMyDashboard: () => api.get('/faculty/dashboard').then((res) => res.data),
}

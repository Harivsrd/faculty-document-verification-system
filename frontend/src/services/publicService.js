import api from './api'

export const publicService = {
  listFaculty: (params) => api.get('/public/faculty', { params }).then((res) => res.data),
  getFaculty: (id) => api.get(`/public/faculty/${id}`).then((res) => res.data),
  getVerifiedEducation: (id) => api.get(`/public/faculty/${id}/education`).then((res) => res.data),
  getVerifiedExperience: (id) => api.get(`/public/faculty/${id}/experience`).then((res) => res.data),
}

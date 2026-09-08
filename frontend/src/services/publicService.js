import api from './api'

export const publicService = {
<<<<<<< HEAD
  search: (keyword) => api.get('/public/faculty', { params: { keyword } }).then((res) => res.data),
  getDetail: (id) => api.get(`/public/faculty/${id}`).then((res) => res.data),
=======
  listFaculty: (params) => api.get('/public/faculty', { params }).then((res) => res.data),
  getFaculty: (id) => api.get(`/public/faculty/${id}`).then((res) => res.data),
  getVerifiedEducation: (id) => api.get(`/public/faculty/${id}/education`).then((res) => res.data),
  getVerifiedExperience: (id) => api.get(`/public/faculty/${id}/experience`).then((res) => res.data),
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}

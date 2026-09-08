import api from './api'

export const publicService = {
  search: (keyword) => api.get('/public/faculty', { params: { keyword } }).then((res) => res.data),
  getDetail: (id) => api.get(`/public/faculty/${id}`).then((res) => res.data),
}

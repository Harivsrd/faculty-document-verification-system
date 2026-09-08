import api from './api'

export const educationService = {
  list: () => api.get('/faculty/education').then((res) => res.data),
  create: (payload) => api.post('/faculty/education', payload).then((res) => res.data),
  update: (id, payload) => api.put(`/faculty/education/${id}`, payload).then((res) => res.data),
  remove: (id) => api.delete(`/faculty/education/${id}`),
  uploadCertificate: (id, file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api
      .post(`/faculty/education/${id}/certificate`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then((res) => res.data)
  },
}

import api from './api'

export const experienceService = {
  list: () => api.get('/faculty/experience').then((res) => res.data),
  create: (payload) => api.post('/faculty/experience', payload).then((res) => res.data),
  update: (id, payload) => api.put(`/faculty/experience/${id}`, payload).then((res) => res.data),
  remove: (id) => api.delete(`/faculty/experience/${id}`),
  uploadCertificate: (id, file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api
      .post(`/faculty/experience/${id}/certificate`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then((res) => res.data)
  },
}

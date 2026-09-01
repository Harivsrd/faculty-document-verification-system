import api from './api'

export const facultyService = {
  getProfile: () => api.get('/faculty/profile').then((res) => res.data),
  updateProfile: (data) => api.put('/faculty/profile', data).then((res) => res.data),
  getDashboard: () => api.get('/faculty/profile/dashboard').then((res) => res.data),

  listEducation: () => api.get('/faculty/education').then((res) => res.data),
  createEducation: (data) => api.post('/faculty/education', data).then((res) => res.data),
  updateEducation: (id, data) => api.put(`/faculty/education/${id}`, data).then((res) => res.data),
  deleteEducation: (id) => api.delete(`/faculty/education/${id}`).then((res) => res.data),
  uploadEducationCertificate: (id, file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api
      .post(`/faculty/education/${id}/certificate`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then((res) => res.data)
  },

  listExperience: () => api.get('/faculty/experience').then((res) => res.data),
  createExperience: (data) => api.post('/faculty/experience', data).then((res) => res.data),
  updateExperience: (id, data) => api.put(`/faculty/experience/${id}`, data).then((res) => res.data),
  deleteExperience: (id) => api.delete(`/faculty/experience/${id}`).then((res) => res.data),
  uploadExperienceCertificate: (id, file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api
      .post(`/faculty/experience/${id}/certificate`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then((res) => res.data)
  },

  listDocuments: () => api.get('/faculty/documents').then((res) => res.data),
  reuploadDocument: (id, file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api
      .post(`/faculty/documents/${id}/reupload`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      .then((res) => res.data)
  },
  deleteDocument: (id) => api.delete(`/faculty/documents/${id}`).then((res) => res.data),
}

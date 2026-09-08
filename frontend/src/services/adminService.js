import api from './api'

export const adminService = {
  listFaculty: (params) => api.get('/admin/faculty', { params }).then((res) => res.data),
<<<<<<< HEAD
  getFacultyDetail: (id) => api.get(`/admin/faculty/${id}`).then((res) => res.data),
=======
  getFaculty: (id) => api.get(`/admin/faculty/${id}`).then((res) => res.data),
  getFacultyEducation: (id) => api.get(`/admin/faculty/${id}/education`).then((res) => res.data),
  getFacultyExperience: (id) => api.get(`/admin/faculty/${id}/experience`).then((res) => res.data),
  getFacultyDocuments: (id) => api.get(`/admin/faculty/${id}/documents`).then((res) => res.data),
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

  getPendingDocuments: (params) => api.get('/admin/documents/pending', { params }).then((res) => res.data),
  approveDocument: (id) => api.put(`/admin/documents/${id}/approve`).then((res) => res.data),
  rejectDocument: (id, reason) =>
    api.put(`/admin/documents/${id}/reject`, { reason }).then((res) => res.data),
  getDocumentHistory: (id) => api.get(`/admin/documents/${id}/history`).then((res) => res.data),

<<<<<<< HEAD
  // Preview/download require the Authorization header, so they're fetched
  // as blobs and turned into object URLs on the client rather than being
  // linked to directly.
  downloadDocument: (id) =>
    api.get(`/admin/documents/${id}/download`, { responseType: 'blob' }).then((res) => res.data),
  previewDocumentBlob: (id) =>
    api.get(`/admin/documents/${id}/preview`, { responseType: 'blob' }).then((res) => res.data),
=======
  getPreviewUrl: (id) => `${api.defaults.baseURL}/admin/documents/${id}/preview`,
  getDownloadUrl: (id) => `${api.defaults.baseURL}/admin/documents/${id}/download`,

  /** Downloads via authenticated blob fetch since <a href> can't carry an Authorization header. */
  downloadDocument: async (id, fileName) => {
    const response = await api.get(`/admin/documents/${id}/download`, { responseType: 'blob' })
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName || `document-${id}`)
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(url)
  },

  previewDocument: async (id) => {
    const response = await api.get(`/admin/documents/${id}/preview`, { responseType: 'blob' })
    return window.URL.createObjectURL(new Blob([response.data]))
  },
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

  getDashboard: () => api.get('/admin/dashboard').then((res) => res.data),
  getAuditLogs: (params) => api.get('/admin/audit-logs', { params }).then((res) => res.data),
}

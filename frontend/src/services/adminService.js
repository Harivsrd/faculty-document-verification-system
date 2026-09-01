import api from './api'

export const adminService = {
  listFaculty: (params) => api.get('/admin/faculty', { params }).then((res) => res.data),
  getFaculty: (id) => api.get(`/admin/faculty/${id}`).then((res) => res.data),
  getFacultyEducation: (id) => api.get(`/admin/faculty/${id}/education`).then((res) => res.data),
  getFacultyExperience: (id) => api.get(`/admin/faculty/${id}/experience`).then((res) => res.data),
  getFacultyDocuments: (id) => api.get(`/admin/faculty/${id}/documents`).then((res) => res.data),

  getPendingDocuments: (params) => api.get('/admin/documents/pending', { params }).then((res) => res.data),
  approveDocument: (id) => api.put(`/admin/documents/${id}/approve`).then((res) => res.data),
  rejectDocument: (id, reason) =>
    api.put(`/admin/documents/${id}/reject`, { reason }).then((res) => res.data),
  getDocumentHistory: (id) => api.get(`/admin/documents/${id}/history`).then((res) => res.data),

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

  getDashboard: () => api.get('/admin/dashboard').then((res) => res.data),
  getAuditLogs: (params) => api.get('/admin/audit-logs', { params }).then((res) => res.data),
}

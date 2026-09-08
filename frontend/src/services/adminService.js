import api from './api'

export const adminService = {
  listFaculty: (params) => api.get('/admin/faculty', { params }).then((res) => res.data),
  getFacultyDetail: (id) => api.get(`/admin/faculty/${id}`).then((res) => res.data),

  getPendingDocuments: (params) => api.get('/admin/documents/pending', { params }).then((res) => res.data),
  approveDocument: (id) => api.put(`/admin/documents/${id}/approve`).then((res) => res.data),
  rejectDocument: (id, reason) =>
    api.put(`/admin/documents/${id}/reject`, { reason }).then((res) => res.data),
  getDocumentHistory: (id) => api.get(`/admin/documents/${id}/history`).then((res) => res.data),

  // Preview/download require the Authorization header, so they're fetched
  // as blobs and turned into object URLs on the client rather than being
  // linked to directly.
  downloadDocument: (id) =>
    api.get(`/admin/documents/${id}/download`, { responseType: 'blob' }).then((res) => res.data),
  previewDocumentBlob: (id) =>
    api.get(`/admin/documents/${id}/preview`, { responseType: 'blob' }).then((res) => res.data),

  getDashboard: () => api.get('/admin/dashboard').then((res) => res.data),
  getAuditLogs: (params) => api.get('/admin/audit-logs', { params }).then((res) => res.data),
}

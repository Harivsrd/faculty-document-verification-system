import api from './api'

export const documentService = {
  myDocuments: () => api.get('/faculty/documents').then((res) => res.data),
}

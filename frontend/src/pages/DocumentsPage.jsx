import { useEffect, useMemo, useState } from 'react'
import MainLayout from '../layouts/MainLayout'
import StatusBadge from '../components/StatusBadge'
import FileUploadButton from '../components/FileUploadButton'
import ConfirmDialog from '../components/ConfirmDialog'
import { facultyService } from '../services/facultyService'

const FILTERS = ['ALL', 'PENDING', 'APPROVED', 'REJECTED']

export default function DocumentsPage() {
  const [documents, setDocuments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [filter, setFilter] = useState('ALL')
  const [deleteTarget, setDeleteTarget] = useState(null)

  const load = () => {
    setLoading(true)
    facultyService
      .listDocuments()
      .then(setDocuments)
      .catch(() => setError('Could not load documents.'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const filtered = useMemo(
    () => (filter === 'ALL' ? documents : documents.filter((d) => d.status === filter)),
    [documents, filter]
  )

  const handleReupload = async (documentId, file) => {
    await facultyService.reuploadDocument(documentId, file)
    load()
  }

  const handleDelete = async () => {
    try {
      await facultyService.deleteDocument(deleteTarget)
      setDeleteTarget(null)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete document.')
    }
  }

  return (
    <MainLayout>
      <h1 className="text-xl font-semibold text-gray-900 mb-1">My Documents</h1>
      <p className="text-sm text-gray-500 mb-6">All certificates you&apos;ve uploaded for education and experience records.</p>

      <div className="flex gap-2 mb-4">
        {FILTERS.map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`px-3 py-1.5 text-xs rounded-md border ${
              filter === f ? 'bg-brand-600 text-white border-brand-600' : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
            }`}
          >
            {f}
          </button>
        ))}
      </div>

      {error && <div className="mb-4 text-sm text-red-600">{error}</div>}

      {loading ? (
        <p className="text-gray-500">Loading...</p>
      ) : filtered.length === 0 ? (
        <p className="text-gray-500">No documents in this category.</p>
      ) : (
        <div className="space-y-3">
          {filtered.map((doc) => (
            <div key={doc.id} className="card flex flex-wrap items-center justify-between gap-3">
              <div>
                <div className="font-medium text-gray-900">{doc.fileName}</div>
                <div className="text-xs text-gray-500">
                  {doc.documentType.replace('_', ' ')} · Uploaded {new Date(doc.uploadedAt).toLocaleDateString()}
                </div>
                {doc.status === 'REJECTED' && doc.rejectionReason && (
                  <div className="text-xs text-red-600 mt-1">Reason: {doc.rejectionReason}</div>
                )}
              </div>
              <div className="flex items-center gap-2">
                <StatusBadge status={doc.status} />
                {doc.status === 'REJECTED' && (
                  <FileUploadButton label="Re-upload" onUpload={(file) => handleReupload(doc.id, file)} />
                )}
                <button className="btn-danger text-xs" onClick={() => setDeleteTarget(doc.id)}>
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <ConfirmDialog
        open={deleteTarget !== null}
        title="Delete document"
        message="This will permanently remove this document. Continue?"
        confirmLabel="Delete"
        danger
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </MainLayout>
  )
}

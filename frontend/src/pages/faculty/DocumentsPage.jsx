import React, { useEffect, useState } from 'react'
import { documentService } from '../../services/documentService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

const FILTERS = ['ALL', 'PENDING', 'APPROVED', 'REJECTED']

export default function DocumentsPage() {
  const [documents, setDocuments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [filter, setFilter] = useState('ALL')

  useEffect(() => {
    documentService
      .myDocuments()
      .then(setDocuments)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load documents'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <LoadingSpinner />

  const filtered = filter === 'ALL' ? documents : documents.filter((d) => d.status === filter)

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-6">My Documents</h1>
      <ErrorAlert message={error} />

      <div className="flex gap-2 mb-4">
        {FILTERS.map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`px-3 py-1.5 rounded-md text-sm font-medium ${
              filter === f ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            }`}
          >
            {f}
          </button>
        ))}
      </div>

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left text-gray-500 border-b border-gray-100">
              <th className="py-2 pr-4">File</th>
              <th className="py-2 pr-4">Type</th>
              <th className="py-2 pr-4">Status</th>
              <th className="py-2 pr-4">Uploaded</th>
              <th className="py-2 pr-4">Notes</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((doc) => (
              <tr key={doc.id} className="border-b border-gray-50 last:border-0">
                <td className="py-3 pr-4">{doc.fileName}</td>
                <td className="py-3 pr-4 text-gray-500">{doc.documentType.replace('_', ' ')}</td>
                <td className="py-3 pr-4"><StatusBadge status={doc.status} /></td>
                <td className="py-3 pr-4 text-gray-500">
                  {doc.uploadedAt ? new Date(doc.uploadedAt).toLocaleDateString() : '—'}
                </td>
                <td className="py-3 pr-4 text-red-600">{doc.rejectionReason || '—'}</td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td colSpan={5} className="py-6 text-center text-gray-400">No documents found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

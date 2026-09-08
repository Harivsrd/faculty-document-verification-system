import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { adminService } from '../../services/adminService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

export default function AdminPendingDocumentsPage() {
  const [data, setData] = useState(null)
  const [page, setPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = (p) => {
    setLoading(true)
    adminService
      .getPendingDocuments({ page: p, size: 10 })
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load pending documents'))
      .finally(() => setLoading(false))
  }

  useEffect(() => load(page), [page])

  const handleQuickApprove = async (docId) => {
    try {
      await adminService.approveDocument(docId)
      load(page)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to approve document')
    }
  }

  if (loading) return <LoadingSpinner />

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-6">Pending Verification</h1>
      <ErrorAlert message={error} />

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left text-gray-500 border-b border-gray-100">
              <th className="py-2 pr-4">Faculty</th>
              <th className="py-2 pr-4">File</th>
              <th className="py-2 pr-4">Type</th>
              <th className="py-2 pr-4">Uploaded</th>
              <th className="py-2 pr-4">Status</th>
              <th className="py-2 pr-4"></th>
            </tr>
          </thead>
          <tbody>
            {data?.content.map((doc) => (
              <tr key={doc.id} className="border-b border-gray-50 last:border-0">
                <td className="py-3 pr-4 font-medium">{doc.facultyName}</td>
                <td className="py-3 pr-4">{doc.fileName}</td>
                <td className="py-3 pr-4 text-gray-500">{doc.documentType.replace('_', ' ')}</td>
                <td className="py-3 pr-4 text-gray-500">
                  {doc.uploadedAt ? new Date(doc.uploadedAt).toLocaleDateString() : '—'}
                </td>
                <td className="py-3 pr-4"><StatusBadge status={doc.status} /></td>
                <td className="py-3 pr-4 flex gap-2">
                  <Link to={`/admin/faculty/${doc.facultyId}`} className="btn-secondary text-xs">
                    Review
                  </Link>
                  <button className="btn-success text-xs" onClick={() => handleQuickApprove(doc.id)}>
                    Quick Approve
                  </button>
                </td>
              </tr>
            ))}
            {data?.content.length === 0 && (
              <tr>
                <td colSpan={6} className="py-6 text-center text-gray-400">
                  No documents awaiting verification.
                </td>
              </tr>
            )}
          </tbody>
        </table>

        {data && data.totalPages > 1 && (
          <div className="flex justify-between items-center mt-4 text-sm">
            <button className="btn-secondary" disabled={page === 0} onClick={() => setPage((p) => Math.max(0, p - 1))}>
              Previous
            </button>
            <span className="text-gray-500">Page {page + 1} of {data.totalPages}</span>
            <button
              className="btn-secondary"
              disabled={page + 1 >= data.totalPages}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  )
}

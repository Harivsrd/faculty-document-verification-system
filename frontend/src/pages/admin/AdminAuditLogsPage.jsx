import React, { useEffect, useState } from 'react'
import { adminService } from '../../services/adminService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

const ACTIONS = [
  '', 'LOGIN', 'LOGOUT', 'CREATE_PROFILE', 'UPDATE_PROFILE',
  'ADD_EDUCATION', 'UPDATE_EDUCATION', 'DELETE_EDUCATION',
  'ADD_EXPERIENCE', 'UPDATE_EXPERIENCE', 'DELETE_EXPERIENCE',
  'UPLOAD_DOCUMENT', 'UPDATE_DOCUMENT', 'DELETE_DOCUMENT',
  'APPROVE_DOCUMENT', 'REJECT_DOCUMENT', 'DOWNLOAD_DOCUMENT',
  'PREVIEW_DOCUMENT', 'REUPLOAD_DOCUMENT',
]

export default function AdminAuditLogsPage() {
  const [action, setAction] = useState('')
  const [page, setPage] = useState(0)
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = (a, p) => {
    setLoading(true)
    adminService
      .getAuditLogs({ action: a || undefined, page: p, size: 20 })
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load audit logs'))
      .finally(() => setLoading(false))
  }

  useEffect(() => load(action, page), [action, page])

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-6">Audit Logs</h1>

      <div className="mb-4">
        <select
          className="input max-w-xs"
          value={action}
          onChange={(e) => { setAction(e.target.value); setPage(0) }}
        >
          {ACTIONS.map((a) => (
            <option key={a} value={a}>{a || 'All actions'}</option>
          ))}
        </select>
      </div>

      <ErrorAlert message={error} />

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div className="card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="text-left text-gray-500 border-b border-gray-100">
                <th className="py-2 pr-4">Timestamp</th>
                <th className="py-2 pr-4">User</th>
                <th className="py-2 pr-4">Action</th>
                <th className="py-2 pr-4">Description</th>
              </tr>
            </thead>
            <tbody>
              {data?.content.map((log) => (
                <tr key={log.id} className="border-b border-gray-50 last:border-0">
                  <td className="py-3 pr-4 text-gray-500 whitespace-nowrap">
                    {new Date(log.timestamp).toLocaleString()}
                  </td>
                  <td className="py-3 pr-4">
                    {log.userName || '—'}
                    <div className="text-xs text-gray-400">{log.userEmail}</div>
                  </td>
                  <td className="py-3 pr-4">
                    <span className="badge bg-gray-100 text-gray-700">{log.action}</span>
                  </td>
                  <td className="py-3 pr-4 text-gray-600">{log.description}</td>
                </tr>
              ))}
              {data?.content.length === 0 && (
                <tr>
                  <td colSpan={4} className="py-6 text-center text-gray-400">No audit log entries found.</td>
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
      )}
    </div>
  )
}

import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { adminService } from '../../services/adminService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

function StatCard({ label, value, accent }) {
  return (
    <div className="card">
      <p className="text-sm text-gray-500">{label}</p>
      <p className={`text-2xl font-semibold mt-1 ${accent || 'text-gray-900'}`}>{value}</p>
    </div>
  )
}

export default function AdminDashboardPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    adminService
      .getDashboard()
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load dashboard'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <LoadingSpinner />
  if (error) return <ErrorAlert message={error} />
  if (!data) return null

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold">Admin Dashboard</h1>

      <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
        <StatCard label="Faculty" value={data.totalFaculty} />
        <StatCard label="Documents" value={data.totalDocuments} />
        <StatCard label="Pending" value={data.pendingDocuments} accent="text-yellow-600" />
        <StatCard label="Approved" value={data.approvedDocuments} accent="text-green-600" />
        <StatCard label="Rejected" value={data.rejectedDocuments} accent="text-red-600" />
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-medium">Pending Verification</h2>
          <Link to="/admin/pending-documents" className="text-sm text-primary-600 font-medium">
            View all
          </Link>
        </div>
        <div className="space-y-3">
          {data.recentPending.length === 0 && (
            <p className="text-gray-400 text-sm">No documents awaiting verification.</p>
          )}
          {data.recentPending.map((doc) => (
            <div key={doc.id} className="flex items-center justify-between border-b border-gray-50 last:border-0 pb-3 last:pb-0">
              <div>
                <p className="font-medium text-sm">{doc.facultyName}</p>
                <p className="text-xs text-gray-500">{doc.fileName} · {doc.documentType.replace('_', ' ')}</p>
              </div>
              <div className="flex items-center gap-3">
                <StatusBadge status={doc.status} />
                <Link to={`/admin/faculty/${doc.facultyId}`} className="btn-secondary text-xs">
                  Review
                </Link>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import StatusBadge from '../components/StatusBadge'
import { adminService } from '../services/adminService'

export default function AdminDashboardPage() {
  const [stats, setStats] = useState(null)
  const [pending, setPending] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    adminService
      .getDashboard()
      .then(setStats)
      .catch(() => setError('Could not load dashboard stats.'))

    adminService
      .getPendingDocuments({ size: 5 })
      .then((res) => setPending(res.content))
      .catch(() => {})
  }, [])

  return (
    <MainLayout>
      <h1 className="text-xl font-semibold text-gray-900 mb-6">Admin Dashboard</h1>

      {error && <div className="mb-4 text-sm text-red-600">{error}</div>}

      {stats && (
        <div className="grid grid-cols-2 md:grid-cols-5 gap-4 mb-8">
          <StatCard label="Faculty" value={stats.totalFaculty} />
          <StatCard label="Documents" value={stats.totalDocuments} />
          <StatCard label="Pending" value={stats.pendingDocuments} accent="text-yellow-600" />
          <StatCard label="Approved" value={stats.approvedDocuments} accent="text-green-600" />
          <StatCard label="Rejected" value={stats.rejectedDocuments} accent="text-red-600" />
        </div>
      )}

      <div className="flex items-center justify-between mb-3">
        <h2 className="font-medium text-gray-900">Pending Verification</h2>
        <Link to="/admin/pending-documents" className="text-sm text-brand-600 hover:underline">
          View all
        </Link>
      </div>

      {pending.length === 0 ? (
        <p className="text-gray-500 text-sm">No documents are currently pending review.</p>
      ) : (
        <div className="space-y-3">
          {pending.map((doc) => (
            <div key={doc.id} className="card flex items-center justify-between">
              <div>
                <div className="font-medium text-gray-900">{doc.facultyName}</div>
                <div className="text-sm text-gray-500">
                  {doc.fileName} · {doc.documentType.replace('_', ' ')}
                </div>
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
      )}
    </MainLayout>
  )
}

function StatCard({ label, value, accent = 'text-gray-900' }) {
  return (
    <div className="card">
      <div className={`text-2xl font-semibold ${accent}`}>{value ?? '-'}</div>
      <div className="text-sm text-gray-500 mt-1">{label}</div>
    </div>
  )
}

import { useEffect, useState } from 'react'
import MainLayout from '../layouts/MainLayout'
import { facultyService } from '../services/facultyService'
import { useAuth } from '../context/AuthContext'

export default function FacultyDashboardPage() {
  const { user } = useAuth()
  const [data, setData] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    facultyService
      .getDashboard()
      .then(setData)
      .catch(() => setError('Could not load dashboard data.'))
  }, [])

  return (
    <MainLayout>
      <h1 className="text-xl font-semibold text-gray-900 mb-1">Welcome, {user?.name}</h1>
      <p className="text-sm text-gray-500 mb-6">Here&apos;s an overview of your profile and submissions.</p>

      {error && <div className="text-sm text-red-600 mb-4">{error}</div>}

      {data && (
        <div className="space-y-6">
          <div className="card">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium text-gray-700">Profile Completion</span>
              <span className="text-sm font-semibold text-brand-700">{data.profileCompletionPercent}%</span>
            </div>
            <div className="w-full bg-gray-100 rounded-full h-2">
              <div
                className="bg-brand-600 h-2 rounded-full transition-all"
                style={{ width: `${data.profileCompletionPercent}%` }}
              />
            </div>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            <StatCard label="Education Records" value={data.educationCount} />
            <StatCard label="Experience Records" value={data.experienceCount} />
            <StatCard label="Approved Documents" value={data.approvedDocuments} accent="text-green-600" />
            <StatCard label="Pending Documents" value={data.pendingDocuments} accent="text-yellow-600" />
            <StatCard label="Rejected Documents" value={data.rejectedDocuments} accent="text-red-600" />
          </div>
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

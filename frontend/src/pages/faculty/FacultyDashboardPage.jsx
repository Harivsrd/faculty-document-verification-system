import React, { useEffect, useState } from 'react'
import { profileService } from '../../services/profileService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

function StatCard({ label, value, accent }) {
  return (
    <div className="card">
      <p className="text-sm text-gray-500">{label}</p>
      <p className={`text-2xl font-semibold mt-1 ${accent || 'text-gray-900'}`}>{value}</p>
    </div>
  )
}

export default function FacultyDashboardPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    profileService
      .getMyDashboard()
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load dashboard'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <LoadingSpinner />
  if (error) return <ErrorAlert message={error} />
  if (!data) return null

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-semibold">Faculty Dashboard</h1>
        <p className="text-gray-500 text-sm mt-1">Your profile and submission overview</p>
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-2">
          <p className="text-sm font-medium text-gray-700">Profile Completion</p>
          <p className="text-sm font-semibold text-primary-700">{data.profileCompletionPercent}%</p>
        </div>
        <div className="w-full bg-gray-100 rounded-full h-2">
          <div
            className="bg-primary-600 h-2 rounded-full transition-all"
            style={{ width: `${data.profileCompletionPercent}%` }}
          />
        </div>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
        <StatCard label="Education Qualifications" value={data.educationCount} />
        <StatCard label="Experience Records" value={data.experienceCount} />
        <StatCard label="Approved Documents" value={data.approvedDocuments} accent="text-green-600" />
        <StatCard label="Pending Documents" value={data.pendingDocuments} accent="text-yellow-600" />
        <StatCard label="Rejected Documents" value={data.rejectedDocuments} accent="text-red-600" />
      </div>
    </div>
  )
}

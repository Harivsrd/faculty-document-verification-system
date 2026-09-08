import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { publicService } from '../../services/publicService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

export default function PublicFacultyDetailPage() {
  const { id } = useParams()
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    publicService
      .getDetail(id)
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Faculty profile not found'))
      .finally(() => setLoading(false))
  }, [id])

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-3xl mx-auto px-4 py-4">
          <Link to="/directory" className="text-sm text-primary-600 font-medium">← Back to directory</Link>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8">
        {loading && <LoadingSpinner />}
        <ErrorAlert message={error} />

        {data && (
          <div className="space-y-6">
            <div className="card">
              <h1 className="text-2xl font-semibold">{data.fullName}</h1>
              <p className="text-gray-500">{data.department} {data.specialization ? `· ${data.specialization}` : ''}</p>
              {(data.city || data.state) && (
                <p className="text-sm text-gray-400 mt-1">{[data.city, data.state].filter(Boolean).join(', ')}</p>
              )}
            </div>

            <div className="card">
              <h2 className="font-medium mb-3">Verified Qualifications</h2>
              {data.verifiedQualifications.length === 0 && (
                <p className="text-gray-400 text-sm">No verified qualifications yet.</p>
              )}
              <ul className="list-disc list-inside text-sm space-y-1">
                {data.verifiedQualifications.map((q, i) => <li key={i}>{q}</li>)}
              </ul>
            </div>

            <div className="card">
              <h2 className="font-medium mb-3">Verified Experience</h2>
              {data.verifiedExperience.length === 0 && (
                <p className="text-gray-400 text-sm">No verified experience yet.</p>
              )}
              <ul className="list-disc list-inside text-sm space-y-1">
                {data.verifiedExperience.map((e, i) => <li key={i}>{e}</li>)}
              </ul>
            </div>
          </div>
        )}
      </main>
    </div>
  )
}

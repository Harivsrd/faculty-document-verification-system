import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { publicService } from '../../services/publicService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

export default function PublicDirectoryPage() {
  const [keyword, setKeyword] = useState('')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = (kw) => {
    setLoading(true)
    publicService
      .search(kw)
      .then(setResults)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load faculty directory'))
      .finally(() => setLoading(false))
  }

  useEffect(() => load(''), [])

  const handleSearch = (e) => {
    e.preventDefault()
    load(keyword)
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-5xl mx-auto px-4 py-4 flex items-center justify-between">
          <span className="font-semibold text-primary-700">Verified Faculty Directory</span>
          <Link to="/login" className="btn-secondary text-sm">Faculty / Admin Login</Link>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-4 py-8">
        <form onSubmit={handleSearch} className="flex gap-2 mb-6">
          <input
            className="input max-w-sm"
            placeholder="Search by name, department, specialization"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button className="btn-primary" type="submit">Search</button>
        </form>

        <ErrorAlert message={error} />

        {loading ? (
          <LoadingSpinner />
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {results.map((f) => (
              <Link key={f.facultyId} to={`/directory/${f.facultyId}`} className="card hover:shadow-md transition-shadow">
                <p className="font-medium">{f.fullName}</p>
                <p className="text-sm text-gray-500">{f.department}</p>
                <p className="text-sm text-gray-500">{f.specialization}</p>
                {(f.city || f.state) && (
                  <p className="text-xs text-gray-400 mt-1">{[f.city, f.state].filter(Boolean).join(', ')}</p>
                )}
              </Link>
            ))}
            {results.length === 0 && (
              <p className="text-gray-400 text-sm col-span-full">No verified faculty found.</p>
            )}
          </div>
        )}
      </main>
    </div>
  )
}

import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { adminService } from '../../services/adminService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

export default function AdminFacultyListPage() {
  const [keyword, setKeyword] = useState('')
  const [page, setPage] = useState(0)
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = (kw, p) => {
    setLoading(true)
    adminService
      .listFaculty({ keyword: kw || undefined, page: p, size: 10 })
      .then(setData)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load faculty'))
      .finally(() => setLoading(false))
  }

  useEffect(() => load(keyword, page), [page])

  const handleSearch = (e) => {
    e.preventDefault()
    setPage(0)
    load(keyword, 0)
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-6">Faculty Directory</h1>

      <form onSubmit={handleSearch} className="flex gap-2 mb-4">
        <input
          className="input max-w-sm"
          placeholder="Search by name, email, department, specialization"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button className="btn-primary" type="submit">Search</button>
      </form>

      <ErrorAlert message={error} />

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div className="card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="text-left text-gray-500 border-b border-gray-100">
                <th className="py-2 pr-4">Name</th>
                <th className="py-2 pr-4">Email</th>
                <th className="py-2 pr-4">Department</th>
                <th className="py-2 pr-4">Specialization</th>
                <th className="py-2 pr-4">Documents</th>
                <th className="py-2 pr-4"></th>
              </tr>
            </thead>
            <tbody>
              {data?.content.map((f) => (
                <tr key={f.facultyId} className="border-b border-gray-50 last:border-0">
                  <td className="py-3 pr-4 font-medium">{f.fullName || '—'}</td>
                  <td className="py-3 pr-4 text-gray-500">{f.email}</td>
                  <td className="py-3 pr-4">{f.department || '—'}</td>
                  <td className="py-3 pr-4">{f.specialization || '—'}</td>
                  <td className="py-3 pr-4">{f.documentCount}</td>
                  <td className="py-3 pr-4">
                    <Link to={`/admin/faculty/${f.facultyId}`} className="text-primary-600 font-medium">
                      View
                    </Link>
                  </td>
                </tr>
              ))}
              {data?.content.length === 0 && (
                <tr>
                  <td colSpan={6} className="py-6 text-center text-gray-400">No faculty found.</td>
                </tr>
              )}
            </tbody>
          </table>

          {data && data.totalPages > 1 && (
            <div className="flex justify-between items-center mt-4 text-sm">
              <button
                className="btn-secondary"
                disabled={page === 0}
                onClick={() => setPage((p) => Math.max(0, p - 1))}
              >
                Previous
              </button>
              <span className="text-gray-500">
                Page {page + 1} of {data.totalPages}
              </span>
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

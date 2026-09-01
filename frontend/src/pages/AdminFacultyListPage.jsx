import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import { adminService } from '../services/adminService'

export default function AdminFacultyListPage() {
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const load = () => {
    setLoading(true)
    adminService
      .listFaculty({ search, page, size: 10 })
      .then(setData)
      .catch(() => setError('Could not load faculty list.'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [page])

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    setPage(0)
    load()
  }

  return (
    <MainLayout>
      <h1 className="text-xl font-semibold text-gray-900 mb-6">Faculty Directory</h1>

      <form onSubmit={handleSearchSubmit} className="flex gap-2 mb-6 max-w-md">
        <input
          className="input"
          placeholder="Search by name, email, department, specialization"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button type="submit" className="btn-primary shrink-0">
          Search
        </button>
      </form>

      {error && <div className="mb-4 text-sm text-red-600">{error}</div>}

      {loading ? (
        <p className="text-gray-500">Loading...</p>
      ) : !data || data.content.length === 0 ? (
        <p className="text-gray-500">No faculty found.</p>
      ) : (
        <>
          <div className="space-y-3">
            {data.content.map((f) => (
              <Link
                key={f.id}
                to={`/admin/faculty/${f.id}`}
                className="card flex items-center justify-between hover:border-brand-300 transition-colors block"
              >
                <div>
                  <div className="font-medium text-gray-900">{f.fullName || f.name}</div>
                  <div className="text-sm text-gray-500">
                    {f.email} {f.department && `· ${f.department}`} {f.specialization && `· ${f.specialization}`}
                  </div>
                </div>
                <span className="text-sm text-brand-600">View →</span>
              </Link>
            ))}
          </div>

          <div className="flex items-center justify-between mt-6">
            <button
              className="btn-secondary text-xs"
              disabled={page === 0}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
            >
              Previous
            </button>
            <span className="text-sm text-gray-500">
              Page {data.page + 1} of {Math.max(1, data.totalPages)}
            </span>
            <button
              className="btn-secondary text-xs"
              disabled={data.last}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </MainLayout>
  )
}

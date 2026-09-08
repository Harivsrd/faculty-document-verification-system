import { useEffect, useState, useCallback } from 'react'
import { useParams, Link } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import AdminDocumentRow from '../components/AdminDocumentRow'
import { adminService } from '../services/adminService'

export default function AdminFacultyDetailPage() {
  const { id } = useParams()
  const [profile, setProfile] = useState(null)
  const [education, setEducation] = useState([])
  const [experience, setExperience] = useState([])
  const [documents, setDocuments] = useState([]);
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  const load = useCallback(() => {
    setLoading(true)
    Promise.all([
      adminService.getFaculty(id),
      adminService.getFacultyEducation(id),
      adminService.getFacultyExperience(id),
      adminService.getFacultyDocuments(id),
    ])
      .then(([p, edu, exp, docs]) => {
        setProfile(p)
        setEducation(edu)
        setExperience(exp)
        setDocuments(docs)
      })
      .catch(() => setError('Could not load faculty profile.'))
      .finally(() => setLoading(false))
  }, [id])

  useEffect(load, [load])

  const documentsFor = (type, refId, refKey) =>
    documents.filter((d) => d.documentType === type && d[refKey] === refId)

  if (loading) {
    return (
      <MainLayout>
        <p className="text-gray-500">Loading faculty profile...</p>
      </MainLayout>
    )
  }

  if (error || !profile) {
    return (
      <MainLayout>
        <p className="text-red-600">{error || 'Faculty not found.'}</p>
      </MainLayout>
    )
  }

  return (
    <MainLayout>
      <Link to="/admin/faculty" className="text-sm text-brand-600 hover:underline">
        ← Back to Faculty Directory
      </Link>

      <div className="card my-4">
        <h1 className="text-xl font-semibold text-gray-900">{profile.fullName || profile.name}</h1>
        <p className="text-sm text-gray-500">{profile.email}</p>
        <div className="text-sm text-gray-600 mt-2 flex flex-wrap gap-x-4">
          {profile.department && <span>Department: {profile.department}</span>}
          {profile.specialization && <span>Specialization: {profile.specialization}</span>}
          {profile.city && <span>Location: {profile.city}{profile.state ? `, ${profile.state}` : ''}</span>}
        </div>
      </div>

      <section className="mb-8">
        <h2 className="font-medium text-gray-900 mb-3">Education</h2>
        {education.length === 0 ? (
          <p className="text-sm text-gray-500">No education records.</p>
        ) : (
          <div className="space-y-3">
            {education.map((e) => (
              <div key={e.id} className="card">
                <div className="font-medium text-gray-900 text-sm">
                  {e.qualificationType} — {e.courseName || e.institution}
                </div>
                <div className="text-xs text-gray-500 mb-3">
                  {e.institution} {e.boardOrUniversity && `· ${e.boardOrUniversity}`}{' '}
                  {e.yearOfPassing && `· ${e.yearOfPassing}`}
                </div>
                <div className="space-y-2">
                  {(e.certificates || []).map((doc) => (
                    <AdminDocumentRow key={doc.id} document={doc} onChanged={load} label="Certificate" />
                  ))}
                  {(!e.certificates || e.certificates.length === 0) && (
                    <p className="text-xs text-gray-400">No certificate uploaded.</p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section className="mb-8">
        <h2 className="font-medium text-gray-900 mb-3">Experience</h2>
        {experience.length === 0 ? (
          <p className="text-sm text-gray-500">No experience records.</p>
        ) : (
          <div className="space-y-3">
            {experience.map((e) => (
              <div key={e.id} className="card">
                <div className="font-medium text-gray-900 text-sm">
                  {e.designation} — {e.organization}
                </div>
                <div className="text-xs text-gray-500 mb-3">
                  {e.startDate} to {e.currentlyWorking ? 'Present' : e.endDate || '—'}
                </div>
                <div className="space-y-2">
                  {(e.certificates || []).map((doc) => (
                    <AdminDocumentRow key={doc.id} document={doc} onChanged={load} label="Certificate" />
                  ))}
                  {(!e.certificates || e.certificates.length === 0) && (
                    <p className="text-xs text-gray-400">No certificate uploaded.</p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <h2 className="font-medium text-gray-900 mb-3">All Documents</h2>
        {documents.length === 0 ? (
          <p className="text-sm text-gray-500">No documents submitted.</p>
        ) : (
          <div className="space-y-2">
            {documents.map((doc) => (
              <AdminDocumentRow key={doc.id} document={doc} onChanged={load} />
            ))}
          </div>
        )}
      </section>
    </MainLayout>
  )
}

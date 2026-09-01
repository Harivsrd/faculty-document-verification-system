import { useEffect, useState } from 'react'
import MainLayout from '../layouts/MainLayout'
import StatusBadge from '../components/StatusBadge'
import FileUploadButton from '../components/FileUploadButton'
import ConfirmDialog from '../components/ConfirmDialog'
import { facultyService } from '../services/facultyService'

const QUALIFICATION_TYPES = ['TENTH', 'INTERMEDIATE', 'DIPLOMA', 'BACHELORS', 'MASTERS', 'PHD', 'OTHER']

const emptyForm = {
  qualificationType: 'TENTH',
  institution: '',
  boardOrUniversity: '',
  courseName: '',
  yearOfPassing: '',
  percentageOrCgpa: '',
}

export default function EducationPage() {
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)
  const [deleteTarget, setDeleteTarget] = useState(null)

  const load = () => {
    setLoading(true)
    facultyService
      .listEducation()
      .then(setRecords)
      .catch(() => setError('Could not load education records.'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const openCreate = () => {
    setForm(emptyForm)
    setEditingId(null)
    setShowForm(true)
  }

  const openEdit = (record) => {
    setForm({
      qualificationType: record.qualificationType,
      institution: record.institution || '',
      boardOrUniversity: record.boardOrUniversity || '',
      courseName: record.courseName || '',
      yearOfPassing: record.yearOfPassing || '',
      percentageOrCgpa: record.percentageOrCgpa || '',
    })
    setEditingId(record.id)
    setShowForm(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const payload = {
      ...form,
      yearOfPassing: form.yearOfPassing ? Number(form.yearOfPassing) : null,
      percentageOrCgpa: form.percentageOrCgpa ? Number(form.percentageOrCgpa) : null,
    }
    try {
      if (editingId) {
        await facultyService.updateEducation(editingId, payload)
      } else {
        await facultyService.createEducation(payload)
      }
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save education record.')
    }
  }

  const handleDelete = async () => {
    try {
      await facultyService.deleteEducation(deleteTarget)
      setDeleteTarget(null)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete record.')
    }
  }

  const handleUploadCertificate = async (educationId, file) => {
    await facultyService.uploadEducationCertificate(educationId, file)
    load()
  }

  const handleReupload = async (documentId, file) => {
    await facultyService.reuploadDocument(documentId, file)
    load()
  }

  return (
    <MainLayout>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-semibold text-gray-900">Education Qualifications</h1>
        <button className="btn-primary" onClick={openCreate}>
          + Add Qualification
        </button>
      </div>

      {error && <div className="mb-4 text-sm text-red-600">{error}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-4 max-w-xl">
          <h2 className="font-medium text-gray-900">{editingId ? 'Edit' : 'Add'} Qualification</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="label">Qualification Type</label>
              <select
                className="input"
                value={form.qualificationType}
                onChange={(e) => setForm({ ...form, qualificationType: e.target.value })}
              >
                {QUALIFICATION_TYPES.map((t) => (
                  <option key={t} value={t}>
                    {t}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="label">Institution</label>
              <input
                required
                className="input"
                value={form.institution}
                onChange={(e) => setForm({ ...form, institution: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Board / University</label>
              <input
                className="input"
                value={form.boardOrUniversity}
                onChange={(e) => setForm({ ...form, boardOrUniversity: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Course Name</label>
              <input
                className="input"
                value={form.courseName}
                onChange={(e) => setForm({ ...form, courseName: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Year of Passing</label>
              <input
                type="number"
                className="input"
                value={form.yearOfPassing}
                onChange={(e) => setForm({ ...form, yearOfPassing: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Percentage / CGPA</label>
              <input
                type="number"
                step="0.01"
                className="input"
                value={form.percentageOrCgpa}
                onChange={(e) => setForm({ ...form, percentageOrCgpa: e.target.value })}
              />
            </div>
          </div>
          <div className="flex gap-2">
            <button type="submit" className="btn-primary">
              Save
            </button>
            <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>
              Cancel
            </button>
          </div>
        </form>
      )}

      {loading ? (
        <p className="text-gray-500">Loading...</p>
      ) : records.length === 0 ? (
        <p className="text-gray-500">No education records yet. Add your first qualification above.</p>
      ) : (
        <div className="space-y-4">
          {records.map((r) => (
            <div key={r.id} className="card">
              <div className="flex items-start justify-between">
                <div>
                  <div className="font-medium text-gray-900">
                    {r.qualificationType} — {r.courseName || r.institution}
                  </div>
                  <div className="text-sm text-gray-500">
                    {r.institution} {r.boardOrUniversity && `· ${r.boardOrUniversity}`}
                  </div>
                  <div className="text-sm text-gray-500">
                    {r.yearOfPassing && `Year: ${r.yearOfPassing}`}
                    {r.percentageOrCgpa && ` · Score: ${r.percentageOrCgpa}`}
                  </div>
                </div>
                <div className="flex gap-2">
                  <button className="btn-secondary text-xs" onClick={() => openEdit(r)}>
                    Edit
                  </button>
                  <button className="btn-danger text-xs" onClick={() => setDeleteTarget(r.id)}>
                    Delete
                  </button>
                </div>
              </div>

              <div className="mt-4 border-t border-gray-100 pt-4">
                <div className="text-sm font-medium text-gray-700 mb-2">Certificate</div>
                {r.certificates && r.certificates.length > 0 ? (
                  <div className="space-y-2">
                    {r.certificates.map((doc) => (
                      <div key={doc.id} className="flex flex-wrap items-center gap-2 text-sm">
                        <span className="text-gray-700">{doc.fileName}</span>
                        <StatusBadge status={doc.status} />
                        {doc.status === 'REJECTED' && (
                          <>
                            <span className="text-xs text-red-600">Reason: {doc.rejectionReason}</span>
                            <FileUploadButton
                              label="Re-upload"
                              onUpload={(file) => handleReupload(doc.id, file)}
                            />
                          </>
                        )}
                      </div>
                    ))}
                  </div>
                ) : (
                  <FileUploadButton label="Upload Certificate" onUpload={(file) => handleUploadCertificate(r.id, file)} />
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      <ConfirmDialog
        open={deleteTarget !== null}
        title="Delete education record"
        message="This will permanently delete this qualification and its certificates. Continue?"
        confirmLabel="Delete"
        danger
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </MainLayout>
  )
}

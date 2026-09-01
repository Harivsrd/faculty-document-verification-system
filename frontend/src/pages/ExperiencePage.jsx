import { useEffect, useState } from 'react'
import MainLayout from '../layouts/MainLayout'
import StatusBadge from '../components/StatusBadge'
import FileUploadButton from '../components/FileUploadButton'
import ConfirmDialog from '../components/ConfirmDialog'
import { facultyService } from '../services/facultyService'

const emptyForm = {
  organization: '',
  designation: '',
  department: '',
  startDate: '',
  endDate: '',
  currentlyWorking: false,
  description: '',
}

export default function ExperiencePage() {
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
      .listExperience()
      .then(setRecords)
      .catch(() => setError('Could not load experience records.'))
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
      organization: record.organization || '',
      designation: record.designation || '',
      department: record.department || '',
      startDate: record.startDate || '',
      endDate: record.endDate || '',
      currentlyWorking: record.currentlyWorking,
      description: record.description || '',
    })
    setEditingId(record.id)
    setShowForm(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.currentlyWorking && form.endDate && form.startDate > form.endDate) {
      setError('Start date cannot be after end date.')
      return
    }
    try {
      if (editingId) {
        await facultyService.updateExperience(editingId, form)
      } else {
        await facultyService.createExperience(form)
      }
      setShowForm(false)
      setError('')
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save experience record.')
    }
  }

  const handleDelete = async () => {
    try {
      await facultyService.deleteExperience(deleteTarget)
      setDeleteTarget(null)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete record.')
    }
  }

  const handleUploadCertificate = async (experienceId, file) => {
    await facultyService.uploadExperienceCertificate(experienceId, file)
    load()
  }

  const handleReupload = async (documentId, file) => {
    await facultyService.reuploadDocument(documentId, file)
    load()
  }

  return (
    <MainLayout>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-semibold text-gray-900">Work Experience</h1>
        <button className="btn-primary" onClick={openCreate}>
          + Add Experience
        </button>
      </div>

      {error && <div className="mb-4 text-sm text-red-600">{error}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-4 max-w-xl">
          <h2 className="font-medium text-gray-900">{editingId ? 'Edit' : 'Add'} Experience</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="label">Organization</label>
              <input
                required
                className="input"
                value={form.organization}
                onChange={(e) => setForm({ ...form, organization: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Designation</label>
              <input
                required
                className="input"
                value={form.designation}
                onChange={(e) => setForm({ ...form, designation: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Department</label>
              <input
                className="input"
                value={form.department}
                onChange={(e) => setForm({ ...form, department: e.target.value })}
              />
            </div>
            <div>
              <label className="label">Start Date</label>
              <input
                type="date"
                required
                className="input"
                value={form.startDate}
                onChange={(e) => setForm({ ...form, startDate: e.target.value })}
              />
            </div>
            {!form.currentlyWorking && (
              <div>
                <label className="label">End Date</label>
                <input
                  type="date"
                  className="input"
                  value={form.endDate}
                  onChange={(e) => setForm({ ...form, endDate: e.target.value })}
                />
              </div>
            )}
          </div>
          <label className="flex items-center gap-2 text-sm text-gray-700">
            <input
              type="checkbox"
              checked={form.currentlyWorking}
              onChange={(e) => setForm({ ...form, currentlyWorking: e.target.checked, endDate: '' })}
            />
            Currently working here
          </label>
          <div>
            <label className="label">Description</label>
            <textarea
              className="input"
              rows={3}
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
            />
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
        <p className="text-gray-500">No experience records yet. Add your first one above.</p>
      ) : (
        <div className="space-y-4">
          {records.map((r) => (
            <div key={r.id} className="card">
              <div className="flex items-start justify-between">
                <div>
                  <div className="font-medium text-gray-900">
                    {r.designation} — {r.organization}
                  </div>
                  <div className="text-sm text-gray-500">
                    {r.startDate} to {r.currentlyWorking ? 'Present' : r.endDate || '—'}
                  </div>
                  {r.description && <div className="text-sm text-gray-600 mt-1">{r.description}</div>}
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
                <div className="text-sm font-medium text-gray-700 mb-2">Experience Certificate</div>
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
        title="Delete experience record"
        message="This will permanently delete this experience entry and its certificates. Continue?"
        confirmLabel="Delete"
        danger
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </MainLayout>
  )
}

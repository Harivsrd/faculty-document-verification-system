import React, { useEffect, useState } from 'react'
import { experienceService } from '../../services/experienceService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

const EMPTY_FORM = {
  organization: '', designation: '', department: '',
  startDate: '', endDate: '', currentlyWorking: false, description: '',
}

export default function ExperiencePage() {
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [uploadingId, setUploadingId] = useState(null)

  const load = () => {
    setLoading(true)
    experienceService
      .list()
      .then(setRecords)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load experience records'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleChange = (e) => {
    const { name, type, value, checked } = e.target
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value })
  }

  const resetForm = () => {
    setForm(EMPTY_FORM)
    setEditingId(null)
    setShowForm(false)
  }

  const startEdit = (record) => {
    setForm({
      organization: record.organization,
      designation: record.designation,
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
    setError(null)
    try {
      if (editingId) {
        await experienceService.update(editingId, form)
      } else {
        await experienceService.create(form)
      }
      resetForm()
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save experience record')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this experience record and its certificate?')) return
    try {
      await experienceService.remove(id)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete record')
    }
  }

  const handleUpload = async (id, file) => {
    if (!file) return
    setUploadingId(id)
    setError(null)
    try {
      await experienceService.uploadCertificate(id, file)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to upload certificate')
    } finally {
      setUploadingId(null)
    }
  }

  if (loading) return <LoadingSpinner />

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold">Experience</h1>
        <button className="btn-primary" onClick={() => { resetForm(); setShowForm(true) }}>
          Add Experience
        </button>
      </div>

      <ErrorAlert message={error} />

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-4">
          <h2 className="font-medium">{editingId ? 'Edit Experience' : 'New Experience'}</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="label">Organization</label>
              <input className="input" name="organization" value={form.organization} onChange={handleChange} required />
            </div>
            <div>
              <label className="label">Designation</label>
              <input className="input" name="designation" value={form.designation} onChange={handleChange} required />
            </div>
            <div>
              <label className="label">Department</label>
              <input className="input" name="department" value={form.department} onChange={handleChange} />
            </div>
            <div>
              <label className="label">Start Date</label>
              <input type="date" className="input" name="startDate" value={form.startDate} onChange={handleChange} required />
            </div>
            <div>
              <label className="label">End Date</label>
              <input
                type="date"
                className="input"
                name="endDate"
                value={form.endDate}
                onChange={handleChange}
                disabled={form.currentlyWorking}
              />
            </div>
            <div className="flex items-center gap-2 mt-6">
              <input type="checkbox" name="currentlyWorking" checked={form.currentlyWorking} onChange={handleChange} id="currentlyWorking" />
              <label htmlFor="currentlyWorking" className="text-sm text-gray-700">Currently working here</label>
            </div>
            <div className="sm:col-span-2">
              <label className="label">Description</label>
              <textarea className="input" name="description" rows={3} value={form.description} onChange={handleChange} />
            </div>
          </div>
          <div className="flex gap-3">
            <button type="submit" className="btn-primary">{editingId ? 'Update' : 'Add'}</button>
            <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
          </div>
        </form>
      )}

      <div className="space-y-4">
        {records.length === 0 && <p className="text-gray-500 text-sm">No experience records yet.</p>}
        {records.map((record) => {
          const cert = record.documents?.[0]
          return (
            <div key={record.id} className="card">
              <div className="flex flex-wrap items-start justify-between gap-3">
                <div>
                  <p className="font-medium">{record.designation} — {record.organization}</p>
                  <p className="text-sm text-gray-500">
                    {record.department && `${record.department} · `}
                    {record.startDate} — {record.currentlyWorking ? 'Present' : record.endDate}
                  </p>
                  {record.description && <p className="text-sm text-gray-600 mt-1">{record.description}</p>}
                </div>
                <div className="flex gap-2">
                  <button className="btn-secondary text-sm" onClick={() => startEdit(record)}>Edit</button>
                  <button className="btn-danger text-sm" onClick={() => handleDelete(record.id)}>Delete</button>
                </div>
              </div>

              <div className="mt-4 pt-4 border-t border-gray-100 flex flex-wrap items-center gap-3">
                {cert ? (
                  <>
                    <StatusBadge status={cert.status} />
                    <span className="text-sm text-gray-600">{cert.fileName}</span>
                    {cert.status === 'REJECTED' && (
                      <span className="text-sm text-red-600">Reason: {cert.rejectionReason}</span>
                    )}
                  </>
                ) : (
                  <span className="text-sm text-gray-400">No certificate uploaded</span>
                )}
                <label className="btn-secondary text-sm cursor-pointer">
                  {uploadingId === record.id ? 'Uploading...' : cert ? 'Re-upload Certificate' : 'Upload Certificate'}
                  <input
                    type="file"
                    accept=".pdf,.jpg,.jpeg,.png"
                    className="hidden"
                    onChange={(e) => handleUpload(record.id, e.target.files[0])}
                  />
                </label>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}

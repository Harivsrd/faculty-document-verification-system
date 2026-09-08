import React, { useEffect, useState } from 'react'
import { educationService } from '../../services/educationService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

const QUALIFICATION_TYPES = ['TENTH', 'INTERMEDIATE', 'DIPLOMA', 'BACHELORS', 'MASTERS', 'PHD', 'OTHER']

const EMPTY_FORM = {
  qualificationType: 'BACHELORS', institution: '', boardOrUniversity: '',
  courseName: '', yearOfPassing: '', percentageOrCgpa: '',
}

export default function EducationPage() {
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)
  const [uploadingId, setUploadingId] = useState(null)

  const load = () => {
    setLoading(true)
    educationService
      .list()
      .then(setRecords)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load education records'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const resetForm = () => {
    setForm(EMPTY_FORM)
    setEditingId(null)
    setShowForm(false)
  }

  const startEdit = (record) => {
    setForm({
      qualificationType: record.qualificationType,
      institution: record.institution,
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
    setError(null)
    const payload = {
      ...form,
      yearOfPassing: form.yearOfPassing ? Number(form.yearOfPassing) : null,
      percentageOrCgpa: form.percentageOrCgpa ? Number(form.percentageOrCgpa) : null,
    }
    try {
      if (editingId) {
        await educationService.update(editingId, payload)
      } else {
        await educationService.create(payload)
      }
      resetForm()
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save education record')
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this qualification and its certificate?')) return
    try {
      await educationService.remove(id)
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
      await educationService.uploadCertificate(id, file)
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
        <h1 className="text-2xl font-semibold">Education</h1>
        <button className="btn-primary" onClick={() => { resetForm(); setShowForm(true) }}>
          Add Qualification
        </button>
      </div>

      <ErrorAlert message={error} />

      {showForm && (
        <form onSubmit={handleSubmit} className="card mb-6 space-y-4">
          <h2 className="font-medium">{editingId ? 'Edit Qualification' : 'New Qualification'}</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="label">Qualification Type</label>
              <select className="input" name="qualificationType" value={form.qualificationType} onChange={handleChange}>
                {QUALIFICATION_TYPES.map((t) => (
                  <option key={t} value={t}>{t}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="label">Institution</label>
              <input className="input" name="institution" value={form.institution} onChange={handleChange} required />
            </div>
            <div>
              <label className="label">Board / University</label>
              <input className="input" name="boardOrUniversity" value={form.boardOrUniversity} onChange={handleChange} />
            </div>
            <div>
              <label className="label">Course Name</label>
              <input className="input" name="courseName" value={form.courseName} onChange={handleChange} />
            </div>
            <div>
              <label className="label">Year of Passing</label>
              <input type="number" className="input" name="yearOfPassing" value={form.yearOfPassing} onChange={handleChange} />
            </div>
            <div>
              <label className="label">Percentage / CGPA</label>
              <input type="number" step="0.01" className="input" name="percentageOrCgpa" value={form.percentageOrCgpa} onChange={handleChange} />
            </div>
          </div>
          <div className="flex gap-3">
            <button type="submit" className="btn-primary">{editingId ? 'Update' : 'Add'}</button>
            <button type="button" className="btn-secondary" onClick={resetForm}>Cancel</button>
          </div>
        </form>
      )}

      <div className="space-y-4">
        {records.length === 0 && <p className="text-gray-500 text-sm">No education records yet.</p>}
        {records.map((record) => {
          const cert = record.documents?.[0]
          return (
            <div key={record.id} className="card">
              <div className="flex flex-wrap items-start justify-between gap-3">
                <div>
                  <p className="font-medium">{record.qualificationType} — {record.institution}</p>
                  <p className="text-sm text-gray-500">
                    {record.courseName && `${record.courseName} · `}
                    {record.boardOrUniversity && `${record.boardOrUniversity} · `}
                    {record.yearOfPassing && `${record.yearOfPassing}`}
                    {record.percentageOrCgpa && ` · ${record.percentageOrCgpa}%`}
                  </p>
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

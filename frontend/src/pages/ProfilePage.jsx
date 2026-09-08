import { useEffect, useState } from 'react'
import MainLayout from '../layouts/MainLayout'
import { facultyService } from '../services/facultyService'

const emptyForm = {
  fullName: '',
  phone: '',
  dateOfBirth: '',
  gender: '',
  address: '',
  city: '',
  state: '',
  specialization: '',
  department: '',
  publiclyVisible: false,
}

export default function ProfilePage() {
  const [form, setForm] = useState(emptyForm)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    facultyService
      .getProfile()
      .then((data) => {
        setForm({
          fullName: data.fullName || '',
          phone: data.phone || '',
          dateOfBirth: data.dateOfBirth || '',
          gender: data.gender || '',
          address: data.address || '',
          city: data.city || '',
          state: data.state || '',
          specialization: data.specialization || '',
          department: data.department || '',
          publiclyVisible: data.publiclyVisible || false,
        })
      })
      .catch(() => setError('Could not load profile.'))
      .finally(() => setLoading(false))
  }, [])

  const handleChange = (field) => (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value
    setForm({ ...form, [field]: value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setMessage('')
    setError('')
    try {
      await facultyService.updateProfile({
        ...form,
        dateOfBirth: form.dateOfBirth || null,
      })
      setMessage('Profile updated successfully.')
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile.')
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <MainLayout>
        <p className="text-gray-500">Loading profile...</p>
      </MainLayout>
    )
  }

  return (
    <MainLayout>
      <h1 className="text-xl font-semibold text-gray-900 mb-6">My Profile</h1>

      {message && <div className="mb-4 text-sm text-green-700 bg-green-50 border border-green-200 rounded-md px-3 py-2">{message}</div>}
      {error && <div className="mb-4 text-sm text-red-700 bg-red-50 border border-red-200 rounded-md px-3 py-2">{error}</div>}

      <form onSubmit={handleSubmit} className="card space-y-4 max-w-2xl">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Field label="Full Name" value={form.fullName} onChange={handleChange('fullName')} />
          <Field label="Phone" value={form.phone} onChange={handleChange('phone')} />
          <Field label="Date of Birth" type="date" value={form.dateOfBirth} onChange={handleChange('dateOfBirth')} />
          <Field label="Gender" value={form.gender} onChange={handleChange('gender')} />
          <Field label="City" value={form.city} onChange={handleChange('city')} />
          <Field label="State" value={form.state} onChange={handleChange('state')} />
          <Field label="Specialization" value={form.specialization} onChange={handleChange('specialization')} />
          <Field label="Department" value={form.department} onChange={handleChange('department')} />
        </div>

        <div>
          <label className="label">Address</label>
          <textarea className="input" rows={3} value={form.address} onChange={handleChange('address')} />
        </div>

        <label className="flex items-center gap-2 text-sm text-gray-700">
          <input type="checkbox" checked={form.publiclyVisible} onChange={handleChange('publiclyVisible')} />
          Show my profile in the public approved faculty directory
        </label>

        <button type="submit" disabled={saving} className="btn-primary">
          {saving ? 'Saving...' : 'Save changes'}
        </button>
      </form>
    </MainLayout>
  )
}

function Field({ label, value, onChange, type = 'text' }) {
  return (
    <div>
      <label className="label">{label}</label>
      <input type={type} className="input" value={value} onChange={onChange} />
    </div>
  )
}

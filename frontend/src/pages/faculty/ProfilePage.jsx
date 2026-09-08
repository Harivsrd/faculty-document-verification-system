import React, { useEffect, useState } from 'react'
import { profileService } from '../../services/profileService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'

const EMPTY = {
  fullName: '', phone: '', dateOfBirth: '', gender: '', address: '',
  city: '', state: '', specialization: '', department: '',
}

export default function ProfilePage() {
  const [form, setForm] = useState(EMPTY)
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(false)

  const loadProfile = async () => {
    try {
      const data = await profileService.getMyProfile()

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
      })
      setEmail(data.email || '')
    } catch (err) {
      console.error('Profile request failed:', err.response?.data || err)
      setError(err.response?.data?.message || 'Unable to load profile')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadProfile()
  }, [])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError(null)
    setSuccess(false)

    try {
      await profileService.updateMyProfile(form)
      await loadProfile()
      setSuccess(true)
      setTimeout(() => setSuccess(false), 2500)
    } catch (err) {
      console.error('Profile save failed:', err.response?.data || err)
      setError(err.response?.data?.message || 'Failed to save profile')
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <LoadingSpinner />

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold mb-1">My Profile</h1>
      <p className="text-gray-500 text-sm mb-6">{email}</p>

      <ErrorAlert message={error} />
      {success && (
        <div className="rounded-md bg-green-50 border border-green-200 text-green-700 px-4 py-3 text-sm mb-4">
          Profile updated successfully.
        </div>
      )}

      <form onSubmit={handleSubmit} className="card space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="label">Full Name</label>
            <input className="input" name="fullName" value={form.fullName} onChange={handleChange} />
          </div>
          <div>
            <label className="label">Phone</label>
            <input className="input" name="phone" value={form.phone} onChange={handleChange} />
          </div>
          <div>
            <label className="label">Date of Birth</label>
            <input type="date" className="input" name="dateOfBirth" value={form.dateOfBirth} onChange={handleChange} />
          </div>
          <div>
            <label className="label">Gender</label>
            <select className="input" name="gender" value={form.gender} onChange={handleChange}>
              <option value="">Select</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>
          <div className="sm:col-span-2">
            <label className="label">Address</label>
            <input className="input" name="address" value={form.address} onChange={handleChange} />
          </div>
          <div>
            <label className="label">City</label>
            <input className="input" name="city" value={form.city} onChange={handleChange} />
          </div>
          <div>
            <label className="label">State</label>
            <input className="input" name="state" value={form.state} onChange={handleChange} />
          </div>
          <div>
            <label className="label">Specialization</label>
            <input className="input" name="specialization" value={form.specialization} onChange={handleChange} />
          </div>
          <div>
            <label className="label">Department</label>
            <input className="input" name="department" value={form.department} onChange={handleChange} />
          </div>
        </div>
        <button type="submit" className="btn-primary" disabled={saving}>
          {saving ? 'Saving...' : 'Save Changes'}
        </button>
      </form>
    </div>
  )
}

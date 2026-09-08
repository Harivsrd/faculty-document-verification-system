import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import ErrorAlert from '../../components/ErrorAlert'

export default function RegisterPage() {
  const { register, loading } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(false)

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    try {
      await register(form)
      setSuccess(true)
      setTimeout(() => navigate('/login'), 1200)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="card w-full max-w-md">
        <h1 className="text-xl font-semibold mb-1">Create Faculty Account</h1>
        <p className="text-sm text-gray-500 mb-6">
          New accounts are registered as Faculty. Administrator accounts are provisioned separately.
        </p>

        <ErrorAlert message={error} />
        {success && (
          <div className="rounded-md bg-green-50 border border-green-200 text-green-700 px-4 py-3 text-sm mb-4">
            Account created! Redirecting to login...
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="label">Full Name</label>
            <input
              type="text"
              name="name"
              className="input"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label className="label">Email</label>
            <input
              type="email"
              name="email"
              className="input"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label className="label">Password</label>
            <input
              type="password"
              name="password"
              className="input"
              minLength={8}
              value={form.password}
              onChange={handleChange}
              required
            />
            <p className="text-xs text-gray-400 mt-1">At least 8 characters.</p>
          </div>
          <button type="submit" className="btn-primary w-full" disabled={loading}>
            {loading ? 'Creating account...' : 'Register'}
          </button>
        </form>

        <p className="text-sm text-gray-500 mt-6 text-center">
          Already have an account?{' '}
          <Link to="/login" className="text-primary-600 font-medium">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  )
}

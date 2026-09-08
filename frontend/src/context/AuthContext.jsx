<<<<<<< HEAD
import React, { createContext, useContext, useState, useCallback } from 'react'
=======
import { createContext, useContext, useEffect, useState, useCallback } from 'react'
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import { authService } from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
<<<<<<< HEAD
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('fcs_user')
    return stored ? JSON.parse(stored) : null
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const login = useCallback(async (credentials) => {
    setLoading(true)
    setError(null)
    try {
      const data = await authService.login(credentials)
      localStorage.setItem('fcs_token', data.token)
      localStorage.setItem('fcs_user', JSON.stringify(data.user))
      setUser(data.user)
      return data.user
    } catch (err) {
      const message = err.response?.data?.message || 'Login failed. Please check your credentials.'
      setError(message)
      throw new Error(message)
    } finally {
      setLoading(false)
    }
  }, [])

  const register = useCallback(async (payload) => {
    setLoading(true)
    setError(null)
    try {
      return await authService.register(payload)
    } catch (err) {
      const message = err.response?.data?.message || 'Registration failed.'
      setError(message)
      throw new Error(message)
    } finally {
      setLoading(false)
    }
=======
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const storedUser = localStorage.getItem('fcs_user')
    const storedToken = localStorage.getItem('fcs_token')
    if (storedUser && storedToken) {
      try {
        setUser(JSON.parse(storedUser))
      } catch {
        localStorage.removeItem('fcs_user')
        localStorage.removeItem('fcs_token')
      }
    }
    setLoading(false)
  }, [])

  const login = useCallback(async (email, password) => {
    const data = await authService.login({ email, password })
    localStorage.setItem('fcs_token', data.token)
    localStorage.setItem('fcs_user', JSON.stringify(data.user))
    setUser(data.user)
    return data.user
  }, [])

  const register = useCallback(async (name, email, password) => {
    const data = await authService.register({ name, email, password })
    localStorage.setItem('fcs_token', data.token)
    localStorage.setItem('fcs_user', JSON.stringify(data.user))
    setUser(data.user)
    return data.user
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('fcs_token')
    localStorage.removeItem('fcs_user')
    setUser(null)
  }, [])

<<<<<<< HEAD
  const value = {
    user,
    isAuthenticated: !!user,
    isAdmin: user?.role === 'ADMIN',
    isFaculty: user?.role === 'FACULTY',
    loading,
    error,
    login,
    register,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
=======
  const isAdmin = user?.role === 'ADMIN'
  const isFaculty = user?.role === 'FACULTY'

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, isAdmin, isFaculty }}>
      {children}
    </AuthContext.Provider>
  )
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}

export function useAuth() {
  const ctx = useContext(AuthContext)
<<<<<<< HEAD
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
=======
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider')
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
  return ctx
}

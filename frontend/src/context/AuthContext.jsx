import { createContext, useContext, useEffect, useState, useCallback } from 'react'
import { authService } from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
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
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('fcs_token')
    localStorage.removeItem('fcs_user')
    setUser(null)
  }, [])

  const isAdmin = user?.role === 'ADMIN'
  const isFaculty = user?.role === 'FACULTY'

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, isAdmin, isFaculty }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider')
  return ctx
}

import React from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import ProtectedRoute from './routes/ProtectedRoute'
import MainLayout from './layouts/MainLayout'

import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'

import FacultyDashboardPage from './pages/faculty/FacultyDashboardPage'
import ProfilePage from './pages/faculty/ProfilePage'
import EducationPage from './pages/faculty/EducationPage'
import ExperiencePage from './pages/faculty/ExperiencePage'
import DocumentsPage from './pages/faculty/DocumentsPage'

import AdminDashboardPage from './pages/admin/AdminDashboardPage'
import AdminFacultyListPage from './pages/admin/AdminFacultyListPage'
import AdminFacultyDetailPage from './pages/admin/AdminFacultyDetailPage'
import AdminPendingDocumentsPage from './pages/admin/AdminPendingDocumentsPage'
import AdminAuditLogsPage from './pages/admin/AdminAuditLogsPage'

import PublicDirectoryPage from './pages/public/PublicDirectoryPage'
import PublicFacultyDetailPage from './pages/public/PublicFacultyDetailPage'

function RootRedirect() {
  const { isAuthenticated, isAdmin } = useAuth()
  if (!isAuthenticated) return <Navigate to="/directory" replace />
  return <Navigate to={isAdmin ? '/admin/dashboard' : '/dashboard'} replace />
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Public */}
          <Route path="/" element={<RootRedirect />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/directory" element={<PublicDirectoryPage />} />
          <Route path="/directory/:id" element={<PublicFacultyDetailPage />} />

          {/* Faculty */}
          <Route
            element={
              <ProtectedRoute requiredRole="FACULTY">
                <MainLayout />
              </ProtectedRoute>
            }
          >
            <Route path="/dashboard" element={<FacultyDashboardPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/education" element={<EducationPage />} />
            <Route path="/experience" element={<ExperiencePage />} />
            <Route path="/documents" element={<DocumentsPage />} />
          </Route>

          {/* Admin */}
          <Route
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <MainLayout />
              </ProtectedRoute>
            }
          >
            <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
            <Route path="/admin/faculty" element={<AdminFacultyListPage />} />
            <Route path="/admin/faculty/:id" element={<AdminFacultyDetailPage />} />
            <Route path="/admin/pending-documents" element={<AdminPendingDocumentsPage />} />
            <Route path="/admin/audit-logs" element={<AdminAuditLogsPage />} />
          </Route>

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  )
}

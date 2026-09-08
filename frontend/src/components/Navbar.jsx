import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  if (!user) return null

  const facultyLinks = [
    { to: '/dashboard', label: 'Dashboard' },
    { to: '/profile', label: 'Profile' },
    { to: '/education', label: 'Education' },
    { to: '/experience', label: 'Experience' },
    { to: '/documents', label: 'Documents' },
  ]

  const adminLinks = [
    { to: '/admin/dashboard', label: 'Dashboard' },
    { to: '/admin/faculty', label: 'Faculty' },
    { to: '/admin/pending-documents', label: 'Pending Documents' },
    { to: '/admin/audit-logs', label: 'Audit Logs' },
  ]

  const links = isAdmin ? adminLinks : facultyLinks

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div className="max-w-6xl mx-auto px-4 flex items-center justify-between h-14">
        <div className="flex items-center gap-6">
          <span className="font-semibold text-brand-700">Faculty Credential System</span>
          <div className="hidden md:flex gap-1">
            {links.map((link) => (
              <Link
                key={link.to}
                to={link.to}
                className={`px-3 py-2 text-sm rounded-md ${
                  location.pathname === link.to
                    ? 'bg-brand-50 text-brand-700 font-medium'
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                {link.label}
              </Link>
            ))}
          </div>
        </div>
        <div className="flex items-center gap-3">
          <span className="text-sm text-gray-500 hidden sm:inline">{user.name} ({user.role})</span>
          <button onClick={handleLogout} className="btn-secondary text-xs">
            Logout
          </button>
        </div>
      </div>
      <div className="md:hidden flex overflow-x-auto px-4 pb-2 gap-1">
        {links.map((link) => (
          <Link
            key={link.to}
            to={link.to}
            className={`px-3 py-1.5 text-xs rounded-md whitespace-nowrap ${
              location.pathname === link.to
                ? 'bg-brand-50 text-brand-700 font-medium'
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            {link.label}
          </Link>
        ))}
      </div>
    </nav>
  )
}

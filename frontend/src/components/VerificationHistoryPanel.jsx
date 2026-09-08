import { useEffect, useState } from 'react'
import { adminService } from '../services/adminService'

export default function VerificationHistoryPanel({ documentId, onClose }) {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    adminService
      .getDocumentHistory(documentId)
      .then(setHistory)
      .finally(() => setLoading(false))
  }, [documentId])

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
      <div className="card max-w-md w-full max-h-[80vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold text-gray-900">Verification History</h3>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            ✕
          </button>
        </div>
        {loading ? (
          <p className="text-sm text-gray-500">Loading...</p>
        ) : history.length === 0 ? (
          <p className="text-sm text-gray-500">No history recorded yet.</p>
        ) : (
          <ul className="space-y-3">
            {history.map((h) => (
              <li key={h.id} className="text-sm border-l-2 border-brand-200 pl-3">
                <div className="font-medium text-gray-800">
                  {h.previousStatus} → {h.newStatus}
                </div>
                <div className="text-gray-500 text-xs">
                  {h.adminName} · {new Date(h.actionTimestamp).toLocaleString()}
                </div>
                {h.comment && <div className="text-gray-600 text-xs mt-1">{h.comment}</div>}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}

import { useState } from 'react'

export default function RejectReasonDialog({ open, onConfirm, onCancel }) {
  const [reason, setReason] = useState('')

  if (!open) return null

  const handleConfirm = () => {
    if (!reason.trim()) return
    onConfirm(reason.trim())
    setReason('')
  }

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
      <div className="card max-w-sm w-full">
        <h3 className="font-semibold text-gray-900 mb-2">Reject document</h3>
        <label className="label">Reason for rejection</label>
        <textarea
          className="input"
          rows={3}
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          placeholder="e.g. Certificate is not clearly readable"
        />
        <div className="flex justify-end gap-2 mt-4">
          <button className="btn-secondary" onClick={onCancel}>
            Cancel
          </button>
          <button className="btn-danger" disabled={!reason.trim()} onClick={handleConfirm}>
            Reject
          </button>
        </div>
      </div>
    </div>
  )
}

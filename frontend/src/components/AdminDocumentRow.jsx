import { useState } from 'react'
import StatusBadge from './StatusBadge'
import RejectReasonDialog from './RejectReasonDialog'
import DocumentPreviewModal from './DocumentPreviewModal'
import VerificationHistoryPanel from './VerificationHistoryPanel'
import { adminService } from '../services/adminService'

export default function AdminDocumentRow({ document, onChanged, label }) {
  const [showReject, setShowReject] = useState(false)
  const [showPreview, setShowPreview] = useState(false)
  const [showHistory, setShowHistory] = useState(false)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')

  const handleApprove = async () => {
    setBusy(true)
    setError('')
    try {
      await adminService.approveDocument(document.id)
      onChanged?.()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to approve document.')
    } finally {
      setBusy(false)
    }
  }

  const handleReject = async (reason) => {
    setBusy(true)
    setError('')
    try {
      await adminService.rejectDocument(document.id, reason)
      setShowReject(false)
      onChanged?.()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to reject document.')
    } finally {
      setBusy(false)
    }
  }

  const handleDownload = () => {
    adminService.downloadDocument(document.id, document.fileName)
  }

  return (
    <div className="border border-gray-200 rounded-md p-3">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <div>
          <div className="font-medium text-gray-900 text-sm">{label || document.fileName}</div>
          <div className="text-xs text-gray-500">
            {document.fileName} · Uploaded {new Date(document.uploadedAt).toLocaleDateString()}
          </div>
          {document.status === 'REJECTED' && document.rejectionReason && (
            <div className="text-xs text-red-600 mt-1">Reason: {document.rejectionReason}</div>
          )}
        </div>
        <StatusBadge status={document.status} />
      </div>

      {error && <div className="text-xs text-red-600 mt-2">{error}</div>}

      <div className="flex flex-wrap gap-2 mt-3">
        <button className="btn-secondary text-xs" onClick={() => setShowPreview(true)}>
          Preview
        </button>
        <button className="btn-secondary text-xs" onClick={handleDownload}>
          Download
        </button>
        <button className="btn-secondary text-xs" onClick={() => setShowHistory(true)}>
          History
        </button>
        {document.status !== 'APPROVED' && (
          <button className="btn-success text-xs" disabled={busy} onClick={handleApprove}>
            Approve
          </button>
        )}
        {document.status !== 'REJECTED' && (
          <button className="btn-danger text-xs" disabled={busy} onClick={() => setShowReject(true)}>
            Reject
          </button>
        )}
      </div>

      {showPreview && <DocumentPreviewModal document={document} onClose={() => setShowPreview(false)} />}
      {showHistory && <VerificationHistoryPanel documentId={document.id} onClose={() => setShowHistory(false)} />}
      <RejectReasonDialog open={showReject} onConfirm={handleReject} onCancel={() => setShowReject(false)} />
    </div>
  )
}

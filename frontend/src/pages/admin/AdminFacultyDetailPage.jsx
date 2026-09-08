import React, { useEffect, useState, useCallback } from 'react'
import { useParams } from 'react-router-dom'
import { adminService } from '../../services/adminService'
import LoadingSpinner from '../../components/LoadingSpinner'
import ErrorAlert from '../../components/ErrorAlert'
import StatusBadge from '../../components/StatusBadge'

function DocumentRow({ doc, onApprove, onReject, onPreview, onDownload, onHistory }) {
  const [rejecting, setRejecting] = useState(false)
  const [reason, setReason] = useState('')

  return (
    <div className="border-b border-gray-50 last:border-0 py-3">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <div>
          <p className="text-sm font-medium">{doc.fileName}</p>
          <p className="text-xs text-gray-500">{doc.documentType.replace('_', ' ')}</p>
        </div>
        <div className="flex items-center gap-2">
          <StatusBadge status={doc.status} />
          <button className="btn-secondary text-xs" onClick={() => onPreview(doc)}>Preview</button>
          <button className="btn-secondary text-xs" onClick={() => onDownload(doc)}>Download</button>
          <button className="btn-secondary text-xs" onClick={() => onHistory(doc)}>History</button>
          {doc.status === 'PENDING' && (
            <>
              <button className="btn-success text-xs" onClick={() => onApprove(doc)}>Approve</button>
              <button className="btn-danger text-xs" onClick={() => setRejecting((v) => !v)}>Reject</button>
            </>
          )}
        </div>
      </div>
      {doc.status === 'REJECTED' && (
        <p className="text-xs text-red-600 mt-1">Reason: {doc.rejectionReason}</p>
      )}
      {rejecting && (
        <div className="mt-2 flex gap-2">
          <input
            className="input text-sm"
            placeholder="Rejection reason"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
          />
          <button
            className="btn-danger text-sm"
            onClick={() => {
              if (!reason.trim()) return
              onReject(doc, reason)
              setRejecting(false)
              setReason('')
            }}
          >
            Confirm
          </button>
        </div>
      )}
    </div>
  )
}

export default function AdminFacultyDetailPage() {
  const { id } = useParams()
  const [detail, setDetail] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [history, setHistory] = useState(null)

  const load = useCallback(() => {
    setLoading(true)
    adminService
      .getFacultyDetail(id)
      .then(setDetail)
      .catch((err) => setError(err.response?.data?.message || 'Failed to load faculty detail'))
      .finally(() => setLoading(false))
  }, [id])

  useEffect(load, [load])

  const handleApprove = async (doc) => {
    try {
      await adminService.approveDocument(doc.id)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to approve document')
    }
  }

  const handleReject = async (doc, reason) => {
    try {
      await adminService.rejectDocument(doc.id, reason)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to reject document')
    }
  }

  const openBlob = (blob, contentType) => {
    const url = window.URL.createObjectURL(new Blob([blob], { type: contentType }))
    window.open(url, '_blank', 'noopener')
    setTimeout(() => window.URL.revokeObjectURL(url), 60_000)
  }

  const handlePreview = async (doc) => {
    try {
      const blob = await adminService.previewDocumentBlob(doc.id)
      openBlob(blob, doc.contentType)
    } catch (err) {
      setError('Failed to preview document')
    }
  }

  const handleDownload = async (doc) => {
    try {
      const blob = await adminService.downloadDocument(doc.id)
      const url = window.URL.createObjectURL(new Blob([blob], { type: doc.contentType }))
      const link = document.createElement('a')
      link.href = url
      link.download = doc.fileName
      document.body.appendChild(link)
      link.click()
      link.remove()
      window.URL.revokeObjectURL(url)
    } catch (err) {
      setError('Failed to download document')
    }
  }

  const handleHistory = async (doc) => {
    try {
      const records = await adminService.getDocumentHistory(doc.id)
      setHistory({ doc, records })
    } catch (err) {
      setError('Failed to load verification history')
    }
  }

  if (loading) return <LoadingSpinner />
  if (error && !detail) return <ErrorAlert message={error} />
  if (!detail) return null

  const { profile, education, experience, documents } = detail

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-semibold">{profile.fullName || profile.email}</h1>
        <p className="text-gray-500 text-sm">
          {profile.department || 'No department'} {profile.specialization ? `· ${profile.specialization}` : ''}
        </p>
      </div>

      <ErrorAlert message={error} />

      <div className="card">
        <h2 className="font-medium mb-3">Education</h2>
        {education.length === 0 && <p className="text-gray-400 text-sm">No education records.</p>}
        {education.map((edu) => (
          <div key={edu.id} className="border-b border-gray-50 last:border-0 py-3">
            <p className="text-sm font-medium">
              {edu.qualificationType} — {edu.institution}
            </p>
            <p className="text-xs text-gray-500 mb-2">
              {edu.courseName} {edu.yearOfPassing ? `· ${edu.yearOfPassing}` : ''}
            </p>
            {edu.documents.map((doc) => (
              <DocumentRow
                key={doc.id}
                doc={doc}
                onApprove={handleApprove}
                onReject={handleReject}
                onPreview={handlePreview}
                onDownload={handleDownload}
                onHistory={handleHistory}
              />
            ))}
          </div>
        ))}
      </div>

      <div className="card">
        <h2 className="font-medium mb-3">Experience</h2>
        {experience.length === 0 && <p className="text-gray-400 text-sm">No experience records.</p>}
        {experience.map((exp) => (
          <div key={exp.id} className="border-b border-gray-50 last:border-0 py-3">
            <p className="text-sm font-medium">{exp.designation} — {exp.organization}</p>
            <p className="text-xs text-gray-500 mb-2">
              {exp.startDate} — {exp.currentlyWorking ? 'Present' : exp.endDate}
            </p>
            {exp.documents.map((doc) => (
              <DocumentRow
                key={doc.id}
                doc={doc}
                onApprove={handleApprove}
                onReject={handleReject}
                onPreview={handlePreview}
                onDownload={handleDownload}
                onHistory={handleHistory}
              />
            ))}
          </div>
        ))}
      </div>

      <div className="card">
        <h2 className="font-medium mb-3">All Documents ({documents.length})</h2>
        {documents.map((doc) => (
          <DocumentRow
            key={doc.id}
            doc={doc}
            onApprove={handleApprove}
            onReject={handleReject}
            onPreview={handlePreview}
            onDownload={handleDownload}
            onHistory={handleHistory}
          />
        ))}
      </div>

      {history && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center p-4 z-50" onClick={() => setHistory(null)}>
          <div className="card max-w-lg w-full" onClick={(e) => e.stopPropagation()}>
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-medium">Verification History — {history.doc.fileName}</h3>
              <button className="text-gray-400 hover:text-gray-600" onClick={() => setHistory(null)}>✕</button>
            </div>
            <div className="space-y-3 max-h-96 overflow-y-auto">
              {history.records.length === 0 && <p className="text-gray-400 text-sm">No history yet.</p>}
              {history.records.map((h) => (
                <div key={h.id} className="text-sm border-b border-gray-50 last:border-0 pb-2">
                  <p>
                    <StatusBadge status={h.previousStatus} /> → <StatusBadge status={h.newStatus} />
                  </p>
                  <p className="text-gray-500 text-xs mt-1">
                    {h.adminName} · {new Date(h.actionTimestamp).toLocaleString()}
                  </p>
                  {h.comment && <p className="text-gray-600 text-xs mt-1">{h.comment}</p>}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

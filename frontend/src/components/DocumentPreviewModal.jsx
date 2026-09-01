import { useEffect, useState } from 'react'
import { adminService } from '../services/adminService'

export default function DocumentPreviewModal({ document, onClose }) {
  const [url, setUrl] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let objectUrl = null
    adminService
      .previewDocument(document.id)
      .then((blobUrl) => {
        objectUrl = blobUrl
        setUrl(blobUrl)
      })
      .catch(() => setError('Could not load preview.'))

    return () => {
      if (objectUrl) window.URL.revokeObjectURL(objectUrl)
    }
  }, [document.id])

  const isImage = document.contentType?.startsWith('image/')
  const isPdf = document.contentType === 'application/pdf'

  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-lg w-full max-w-3xl h-[85vh] flex flex-col">
        <div className="flex items-center justify-between px-4 py-3 border-b border-gray-200">
          <span className="font-medium text-gray-900 truncate">{document.fileName}</span>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            ✕
          </button>
        </div>
        <div className="flex-1 overflow-auto bg-gray-100 flex items-center justify-center">
          {error && <p className="text-red-600 text-sm">{error}</p>}
          {!error && !url && <p className="text-gray-500 text-sm">Loading preview...</p>}
          {url && isImage && <img src={url} alt={document.fileName} className="max-w-full max-h-full object-contain" />}
          {url && isPdf && <iframe title="Document preview" src={url} className="w-full h-full" />}
          {url && !isImage && !isPdf && (
            <p className="text-gray-500 text-sm">Preview not supported for this file type. Use Download instead.</p>
          )}
        </div>
      </div>
    </div>
  )
}

import { useRef, useState } from 'react'

export default function FileUploadButton({ onUpload, label = 'Upload' }) {
  const inputRef = useRef(null)
  const [uploading, setUploading] = useState(false)
  const [error, setError] = useState('')

  const handleChange = async (e) => {
    const file = e.target.files?.[0]
    if (!file) return
    setUploading(true)
    setError('')
    try {
      await onUpload(file)
    } catch (err) {
      setError(err.response?.data?.message || 'Upload failed')
    } finally {
      setUploading(false)
      if (inputRef.current) inputRef.current.value = ''
    }
  }

  return (
    <div className="inline-flex flex-col items-start">
      <label className="btn-secondary text-xs cursor-pointer">
        {uploading ? 'Uploading...' : label}
        <input
          ref={inputRef}
          type="file"
          accept=".pdf,.jpg,.jpeg,.png"
          className="hidden"
          onChange={handleChange}
          disabled={uploading}
        />
      </label>
      {error && <span className="text-xs text-red-600 mt-1">{error}</span>}
    </div>
  )
}

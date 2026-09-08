import React from 'react'

const STYLES = {
  PENDING: 'badge-pending',
  APPROVED: 'badge-approved',
  REJECTED: 'badge-rejected',
}

export default function StatusBadge({ status }) {
  return <span className={STYLES[status] || 'badge bg-gray-100 text-gray-800'}>{status}</span>
}

<<<<<<< HEAD
import React from 'react'

const STYLES = {
  PENDING: 'badge-pending',
  APPROVED: 'badge-approved',
  REJECTED: 'badge-rejected',
}

export default function StatusBadge({ status }) {
  return <span className={STYLES[status] || 'badge bg-gray-100 text-gray-800'}>{status}</span>
=======
export default function StatusBadge({ status }) {
  const classMap = {
    PENDING: 'badge-pending',
    APPROVED: 'badge-approved',
    REJECTED: 'badge-rejected',
  }
  return <span className={classMap[status] || 'badge bg-gray-100 text-gray-700'}>{status}</span>
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}

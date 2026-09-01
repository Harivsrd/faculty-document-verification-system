export default function StatusBadge({ status }) {
  const classMap = {
    PENDING: 'badge-pending',
    APPROVED: 'badge-approved',
    REJECTED: 'badge-rejected',
  }
  return <span className={classMap[status] || 'badge bg-gray-100 text-gray-700'}>{status}</span>
}

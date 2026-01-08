/**
 * Admin Notification JavaScript
 * Dựa trên notification.js cho user
 * @author VietTech Team
 */

// Hàm cập nhật badge thông báo cho Admin
function updateAdminNotificationBadge() {
    fetch(`${contextPath}/api/notifications/unread-count`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const badge = document.querySelector('#adminNotificationBell .notification-badge');

            if (data.success && data.count > 0) {
                if (badge) {
                    badge.textContent = data.count > 99 ? '99+' : data.count;
                    badge.style.display = 'flex';
                    badge.classList.add('new');
                    setTimeout(() => badge.classList.remove('new'), 1500);
                }

                // Animation chuông rung
                const bellIcon = document.querySelector('#adminNotificationBell .fa-bell');
                if (bellIcon && !bellIcon.classList.contains('ringing')) {
                    bellIcon.classList.add('ringing');
                    setTimeout(() => bellIcon.classList.remove('ringing'), 1000);
                }
            } else if (badge) {
                badge.style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Error updating admin notification badge:', error);
        });
}

// Hàm định dạng thời gian thông báo
function formatAdminNotificationTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'Vừa xong';
    if (diffMins < 60) return `${diffMins} phút trước`;
    if (diffHours < 24) return `${diffHours} giờ trước`;
    if (diffDays < 7) return `${diffDays} ngày trước`;

    return date.toLocaleDateString('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

// Hàm lấy icon class theo loại thông báo cho Admin
function getAdminNotificationIconClass(type) {
    switch (type) {
        case 'order': return 'fas fa-shopping-cart text-primary';
        case 'new_order': return 'fas fa-cart-plus text-success';
        case 'order_cancelled': return 'fas fa-times-circle text-danger';
        case 'low_stock': return 'fas fa-exclamation-triangle text-warning';
        case 'new_review': return 'fas fa-star text-warning';
        case 'new_user': return 'fas fa-user-plus text-info';
        case 'system': return 'fas fa-cog text-secondary';
        case 'promotion': return 'fas fa-tag text-success';
        default: return 'fas fa-info-circle text-secondary';
    }
}

// Hàm escape HTML để tránh XSS
function escapeAdminHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Hàm cập nhật danh sách thông báo trong dropdown Admin
function updateAdminNotificationDropdown() {
    const dropdownBody = document.querySelector('#adminNotificationBell .notification-dropdown-body');
    if (!dropdownBody) return;

    // Hiển thị loading
    dropdownBody.innerHTML = `
        <div class="notification-loading text-center py-4">
            <div class="spinner-border spinner-border-sm text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2 text-muted small">Đang tải thông báo...</p>
        </div>
    `;

    fetch(`${contextPath}/api/notifications/recent`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success && data.notifications && data.notifications.length > 0) {
                let html = '';
                data.notifications.forEach(notif => {
                    const iconClass = getAdminNotificationIconClass(notif.type);
                    const timeAgo = formatAdminNotificationTime(notif.createdAt);
                    const unreadClass = notif.read ? '' : 'unread';

                    html += `
                        <a href="${contextPath}/admin/notifications/read?id=${notif.notificationId}"
                           class="notification-dropdown-item ${unreadClass}">
                            <div class="notification-item-icon">
                                <i class="${iconClass}"></i>
                            </div>
                            <div class="notification-item-content">
                                <div class="notification-item-title">${escapeAdminHtml(notif.title)}</div>
                                <div class="notification-item-message">${escapeAdminHtml(notif.message)}</div>
                                <div class="notification-item-time">
                                    <i class="fas fa-clock"></i> ${timeAgo}
                                </div>
                            </div>
                            ${notif.read ? '' : '<div class="notification-item-unread-dot"></div>'}
                        </a>
                    `;
                });

                dropdownBody.innerHTML = html;
            } else {
                dropdownBody.innerHTML = `
                    <div class="notification-empty">
                        <i class="fas fa-bell-slash"></i>
                        <p>Không có thông báo mới</p>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('Error loading admin notifications:', error);
            dropdownBody.innerHTML = `
                <div class="notification-error text-center py-4">
                    <i class="fas fa-exclamation-triangle text-warning"></i>
                    <p class="mt-2 text-muted small">Không thể tải thông báo</p>
                    <button class="btn btn-sm btn-outline-primary mt-2" onclick="updateAdminNotificationDropdown()">
                        <i class="fas fa-redo"></i> Thử lại
                    </button>
                </div>
            `;
        });
}

// Setup notification dropdown cho Admin
function setupAdminNotificationDropdown() {
    const notificationBell = document.getElementById('adminNotificationBell');
    if (!notificationBell) return;

    // Khi dropdown sắp mở, cập nhật nội dung
    const dropdownToggle = notificationBell.querySelector('[data-bs-toggle="dropdown"]');
    if (dropdownToggle) {
        dropdownToggle.addEventListener('click', function() {
            updateAdminNotificationDropdown();
        });
    }

    // Xử lý click "Đánh dấu tất cả đã đọc"
    const markAllReadBtn = notificationBell.querySelector('.mark-all-read');
    if (markAllReadBtn) {
        markAllReadBtn.addEventListener('click', function(e) {
            e.preventDefault();
            markAllAdminNotificationsRead();
        });
    }
}

// Đánh dấu tất cả thông báo đã đọc
function markAllAdminNotificationsRead() {
    fetch(`${contextPath}/api/notifications/mark-all-read`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Cập nhật lại badge và dropdown
            updateAdminNotificationBadge();
            updateAdminNotificationDropdown();

            // Hiển thị thông báo thành công
            showAdminToast('Đã đánh dấu tất cả thông báo đã đọc', 'success');
        }
    })
    .catch(error => {
        console.error('Error marking all notifications as read:', error);
    });
}

// Hiển thị toast notification
function showAdminToast(message, type = 'info') {
    // Tạo toast element
    const toastContainer = document.getElementById('adminToastContainer') || createToastContainer();

    const toast = document.createElement('div');
    toast.className = `toast admin-toast ${type}`;
    toast.innerHTML = `
        <div class="toast-body">
            <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'}"></i>
            <span>${message}</span>
        </div>
    `;

    toastContainer.appendChild(toast);

    // Auto show and hide
    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Tạo toast container nếu chưa có
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'adminToastContainer';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}

// Khởi tạo khi DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Setup dropdown
    setupAdminNotificationDropdown();

    // Cập nhật badge ngay khi load
    updateAdminNotificationBadge();

    // Cập nhật badge định kỳ mỗi 30 giây
    setInterval(updateAdminNotificationBadge, 30000);
});

// Thêm CSS cho admin notification
const adminNotifStyle = document.createElement('style');
adminNotifStyle.textContent = `
    /* Admin Notification Bell */
    #adminNotificationBell {
        position: relative;
    }
    
    #adminNotificationBell .notification-bell-btn {
        background: none;
        border: none;
        padding: 8px 12px;
        cursor: pointer;
        position: relative;
        color: #64748b;
        transition: color 0.2s ease;
    }
    
    #adminNotificationBell .notification-bell-btn:hover {
        color: #2563eb;
    }
    
    #adminNotificationBell .notification-bell-btn .fa-bell {
        font-size: 1.25rem;
    }
    
    #adminNotificationBell .notification-badge {
        position: absolute;
        top: 2px;
        right: 4px;
        background: #ef4444;
        color: white;
        font-size: 0.65rem;
        font-weight: 600;
        min-width: 18px;
        height: 18px;
        border-radius: 9px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 4px;
    }
    
    #adminNotificationBell .notification-badge.new {
        animation: badgePulse 0.5s ease;
    }
    
    @keyframes badgePulse {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.2); }
    }
    
    /* Bell ringing animation */
    .fa-bell.ringing {
        animation: bellRing 0.8s ease;
    }
    
    @keyframes bellRing {
        0%, 100% { transform: rotate(0); }
        10%, 30%, 50%, 70%, 90% { transform: rotate(-12deg); }
        20%, 40%, 60%, 80% { transform: rotate(12deg); }
    }
    
    /* Notification Dropdown */
    .notification-dropdown {
        width: 380px;
        max-height: 500px;
        padding: 0;
        border: none;
        border-radius: 12px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
        overflow: hidden;
    }
    
    .notification-dropdown-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 20px;
        background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
        color: white;
    }
    
    .notification-dropdown-header h6 {
        margin: 0;
        font-weight: 600;
        font-size: 1rem;
    }
    
    .notification-dropdown-header .view-all-link {
        color: rgba(255, 255, 255, 0.9);
        font-size: 0.8rem;
        text-decoration: none;
        transition: color 0.2s ease;
    }
    
    .notification-dropdown-header .view-all-link:hover {
        color: white;
        text-decoration: underline;
    }
    
    .notification-dropdown-body {
        max-height: 350px;
        overflow-y: auto;
    }
    
    .notification-dropdown-item {
        display: flex;
        align-items: flex-start;
        gap: 12px;
        padding: 14px 20px;
        text-decoration: none;
        color: inherit;
        border-bottom: 1px solid #f1f5f9;
        transition: background-color 0.2s ease;
        position: relative;
    }
    
    .notification-dropdown-item:hover {
        background-color: #f8fafc;
    }
    
    .notification-dropdown-item.unread {
        background-color: #eff6ff;
    }
    
    .notification-dropdown-item.unread:hover {
        background-color: #dbeafe;
    }
    
    .notification-item-icon {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        background: #f1f5f9;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
    }
    
    .notification-item-icon i {
        font-size: 1rem;
    }
    
    .notification-item-content {
        flex: 1;
        min-width: 0;
    }
    
    .notification-item-title {
        font-weight: 600;
        font-size: 0.9rem;
        color: #1e293b;
        margin-bottom: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    
    .notification-item-message {
        font-size: 0.8rem;
        color: #64748b;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        line-height: 1.4;
    }
    
    .notification-item-time {
        font-size: 0.7rem;
        color: #94a3b8;
        margin-top: 4px;
    }
    
    .notification-item-time i {
        margin-right: 4px;
    }
    
    .notification-item-unread-dot {
        width: 8px;
        height: 8px;
        background: #2563eb;
        border-radius: 50%;
        flex-shrink: 0;
        margin-top: 6px;
    }
    
    .notification-dropdown-footer {
        padding: 12px 20px;
        background: #f8fafc;
        border-top: 1px solid #e2e8f0;
        text-align: center;
    }
    
    .notification-dropdown-footer .mark-all-read {
        color: #2563eb;
        font-size: 0.85rem;
        text-decoration: none;
        font-weight: 500;
        transition: color 0.2s ease;
    }
    
    .notification-dropdown-footer .mark-all-read:hover {
        color: #1d4ed8;
        text-decoration: underline;
    }
    
    .notification-dropdown-footer .mark-all-read i {
        margin-right: 6px;
    }
    
    /* Empty & Error states */
    .notification-empty,
    .notification-error {
        padding: 40px 20px;
        text-align: center;
        color: #94a3b8;
    }
    
    .notification-empty i,
    .notification-error i {
        font-size: 2.5rem;
        margin-bottom: 12px;
        display: block;
    }
    
    .notification-empty p,
    .notification-error p {
        margin: 0;
        font-size: 0.9rem;
    }
    
    /* Loading state */
    .notification-loading {
        padding: 40px 20px;
    }
    
    /* Admin Toast */
    .admin-toast {
        background: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        opacity: 0;
        transform: translateX(100%);
        transition: all 0.3s ease;
    }
    
    .admin-toast.show {
        opacity: 1;
        transform: translateX(0);
    }
    
    .admin-toast .toast-body {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 12px 16px;
    }
    
    .admin-toast.success .toast-body i {
        color: #22c55e;
    }
    
    .admin-toast.info .toast-body i {
        color: #3b82f6;
    }
    
    /* Scrollbar for dropdown */
    .notification-dropdown-body::-webkit-scrollbar {
        width: 6px;
    }
    
    .notification-dropdown-body::-webkit-scrollbar-track {
        background: #f1f5f9;
    }
    
    .notification-dropdown-body::-webkit-scrollbar-thumb {
        background: #cbd5e1;
        border-radius: 3px;
    }
    
    .notification-dropdown-body::-webkit-scrollbar-thumb:hover {
        background: #94a3b8;
    }
`;
document.head.appendChild(adminNotifStyle);


// assets/js/notification.js

// Hàm cập nhật badge thông báo
function updateNotificationBadge() {
    // Kiểm tra nếu không đăng nhập thì không làm gì
    if (typeof isLoggedIn === 'undefined' || !isLoggedIn) {
        console.log('User not logged in, skipping notification update');
        return;
    }

    fetch(`${contextPath}/api/notifications/unread-count`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const badge = document.querySelector('.notification-badge');

            if (data.success && data.count > 0) {
                if (!badge) {
                    // Tạo badge mới nếu chưa có
                    const bell = document.querySelector('.notification-bell');
                    if (bell) {
                        const newBadge = document.createElement('span');
                        newBadge.className = 'notification-badge new';
                        newBadge.textContent = data.count;
                        bell.appendChild(newBadge);
                    }
                } else {
                    // Cập nhật số trên badge hiện có
                    badge.textContent = data.count;
                    badge.classList.add('new');
                    setTimeout(() => badge.classList.remove('new'), 1500);
                }

                // Thêm class để biết có thông báo mới
                const bellIcon = document.querySelector('.notification-bell i');
                if (bellIcon && !bellIcon.classList.contains('has-notifications')) {
                    bellIcon.classList.add('has-notifications');
                    // Animation chuông rung nhẹ
                    bellIcon.style.animation = 'bellRing 1s ease';
                    setTimeout(() => {
                        bellIcon.style.animation = '';
                    }, 1000);
                }
            } else if (badge) {
                // Nếu không có thông báo, xóa badge
                badge.remove();

                // Xóa class thông báo mới
                const bellIcon = document.querySelector('.notification-bell i');
                if (bellIcon) {
                    bellIcon.classList.remove('has-notifications');
                }
            }
        })
        .catch(error => {
            console.error('Error updating notification badge:', error);
        });
}

// Hàm định dạng thời gian thông báo
function formatNotificationTime(dateString) {
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

// Hàm cập nhật danh sách thông báo trong dropdown
function updateNotificationDropdown() {
    // Kiểm tra nếu không đăng nhập thì không làm gì
    if (typeof isLoggedIn === 'undefined' || !isLoggedIn) return;

    const dropdownBody = document.querySelector('.notification-dropdown-body');
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
                    const iconClass = getNotificationIconClass(notif.type);
                    const timeAgo = formatNotificationTime(notif.createdAt);
                    const unreadClass = notif.read ? '' : 'unread';

                    html += `
                        <a href="${contextPath}/notifications/quick-read?notificationId=${notif.notificationId}&redirect=profile"
                           class="notification-dropdown-item ${unreadClass}">
                            <div class="notification-item-icon">
                                <i class="${iconClass}"></i>
                            </div>
                            <div class="notification-item-content">
                                <div class="notification-item-title">${escapeHtml(notif.title)}</div>
                                <div class="notification-item-message">${escapeHtml(notif.message)}</div>
                                <div class="notification-item-time">${timeAgo}</div>
                            </div>
                            ${notif.read ? '' : '<div class="notification-item-unread-dot"></div>'}
                        </a>
                    `;
                });

                dropdownBody.innerHTML = html;
            } else {
                dropdownBody.innerHTML = `
                    <div class="notification-empty">
                        <i class="bi bi-bell-slash"></i>
                        <p>Không có thông báo</p>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('Error loading notifications:', error);
            dropdownBody.innerHTML = `
                <div class="notification-error text-center py-4">
                    <i class="bi bi-exclamation-triangle text-warning"></i>
                    <p class="mt-2 text-muted small">Không thể tải thông báo</p>
                </div>
            `;
        });
}

// Hàm lấy icon class theo loại thông báo
function getNotificationIconClass(type) {
    switch (type) {
        case 'order': return 'bi bi-box-seam text-primary';
        case 'promotion': return 'bi bi-tag-fill text-success';
        case 'system': return 'bi bi-gear-fill text-warning';
        case 'voucher': return 'bi bi-ticket-perforated text-info';
        default: return 'bi bi-info-circle text-secondary';
    }
}

// Hàm escape HTML để tránh XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Fix vấn đề dropdown đóng khi click vào liên kết
function setupNotificationDropdownFix() {
    const notificationBell = document.getElementById('notificationBell');
    const notificationDropdown = document.querySelector('.notification-dropdown');

    if (!notificationBell || !notificationDropdown) return;

    // 1. Khi dropdown sắp mở, cập nhật nội dung
    notificationBell.addEventListener('show.bs.dropdown', function() {
        updateNotificationDropdown();
    });

    // 2. Ngăn dropdown đóng khi click vào liên kết bên trong
    notificationDropdown.addEventListener('click', function(event) {
        // Nếu click vào liên kết, ngăn sự kiện lan truyền
        if (event.target.tagName === 'A' || event.target.closest('a')) {
            event.stopPropagation();
            console.log('Notification link clicked:', event.target.href);
        }
    });
}

// Khởi tạo khi DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Kiểm tra contextPath
    if (typeof contextPath === 'undefined') {
        console.warn('contextPath is not defined. Using default "/"');
        window.contextPath = '';
    }

    // Cài đặt fix cho dropdown
    setupNotificationDropdownFix();

    // Cập nhật badge ngay khi load
    updateNotificationBadge();

    // Cập nhật badge định kỳ mỗi 30 giây
    if (isLoggedIn) {
        setInterval(updateNotificationBadge, 30000); // 30 giây
    }
});

// Thêm CSS animation
const style = document.createElement('style');
style.textContent = `
    @keyframes bellRing {
        0%, 100% { transform: rotate(0); }
        10%, 30%, 50%, 70%, 90% { transform: rotate(-10deg); }
        20%, 40%, 60%, 80% { transform: rotate(10deg); }
    }
    
    .has-notifications {
        position: relative;
    }
    
    .has-notifications::after {
        content: '';
        position: absolute;
        top: -2px;
        right: -2px;
        width: 8px;
        height: 8px;
        background-color: #dc3545;
        border-radius: 50%;
        animation: pulse 2s infinite;
    }
    
    @keyframes pulse {
        0%, 100% { transform: scale(1); opacity: 1; }
        50% { transform: scale(1.2); opacity: 0.8; }
    }
    
    .notification-loading {
        min-height: 100px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
    
    .notification-error {
        min-height: 100px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
`;
document.head.appendChild(style);
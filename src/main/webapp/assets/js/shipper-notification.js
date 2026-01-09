// assets/js/shipper-notification.js
// Hệ thống thông báo cho trang Shipper

(function() {
    'use strict';

    // Cấu hình
    const CONFIG = {
        pollInterval: 60000, // 60 giây kiểm tra thông báo mới (1 phút)
        maxNotifications: 10,
        soundEnabled: true
    };

    // Context path - sẽ được set từ JSP
    let contextPath = '';

    // Khởi tạo
    function init() {
        // Lấy contextPath từ biến global
        contextPath = window.shipperContextPath || '';

        // Load thông báo ban đầu
        loadNotifications();

        // Cập nhật số thông báo chưa đọc
        updateUnreadCount();

        // Bắt đầu polling
        startPolling();

        // Bind sự kiện
        bindEvents();

        console.log('📢 Shipper Notification System initialized');
    }

    // Bind các sự kiện
    function bindEvents() {
        // Toggle dropdown khi click chuông
        const bell = document.getElementById('shipperNotificationBell');
        const dropdown = document.getElementById('shipperNotificationDropdown');

        if (bell && dropdown) {
            bell.addEventListener('click', function(e) {
                e.stopPropagation();
                dropdown.classList.toggle('show');

                if (dropdown.classList.contains('show')) {
                    loadNotifications();
                }
            });

            // Đóng dropdown khi click ngoài
            document.addEventListener('click', function(e) {
                if (!bell.contains(e.target) && !dropdown.contains(e.target)) {
                    dropdown.classList.remove('show');
                }
            });
        }

        // Đánh dấu tất cả đã đọc
        const btnMarkAll = document.getElementById('btnMarkAllRead');
        if (btnMarkAll) {
            btnMarkAll.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                markAllAsRead();
            });
        }

        // Xem tất cả thông báo
        const btnViewAll = document.getElementById('btnViewAllNotifications');
        if (btnViewAll) {
            btnViewAll.addEventListener('click', function(e) {
                e.preventDefault();
                openNotificationModal();
            });
        }
    }

    // Cập nhật số thông báo chưa đọc (số đơn mới trên sàn)
    function updateUnreadCount() {
        fetch(`${contextPath}/api/shipper/notifications`)
            .then(response => response.json())
            .then(data => {
                const badge = document.getElementById('shipperNotificationBadge');
                const bell = document.querySelector('#shipperNotificationBell i');

                if (data.success && data.unreadCount > 0) {
                    if (badge) {
                        badge.textContent = data.unreadCount > 99 ? '99+' : data.unreadCount;
                        badge.style.display = 'flex';
                    }
                    if (bell) {
                        bell.classList.add('has-notifications');
                    }
                } else {
                    if (badge) {
                        badge.style.display = 'none';
                    }
                    if (bell) {
                        bell.classList.remove('has-notifications');
                    }
                }
            })
            .catch(error => {
                console.error('Error fetching unread count:', error);
            });
    }

    // Load danh sách thông báo từ API shipper
    function loadNotifications() {
        const body = document.getElementById('shipperNotificationBody');
        if (!body) return;

        body.innerHTML = `
            <div class="notification-loading">
                <i class="fas fa-spinner fa-spin"></i>
                <p>Đang tải...</p>
            </div>
        `;

        fetch(`${contextPath}/api/shipper/notifications`)
            .then(response => response.json())
            .then(data => {
                if (data.success && data.notifications && data.notifications.length > 0) {
                    renderNotifications(data.notifications);
                } else {
                    body.innerHTML = `
                        <div class="notification-empty">
                            <i class="fas fa-bell-slash"></i>
                            <p>Không có thông báo</p>
                        </div>
                    `;
                }
            })
            .catch(error => {
                console.error('Error loading notifications:', error);
                body.innerHTML = `
                    <div class="notification-error">
                        <i class="fas fa-exclamation-triangle"></i>
                        <p>Không thể tải thông báo</p>
                    </div>
                `;
            });
    }

    // Render danh sách thông báo
    function renderNotifications(notifications) {
        const body = document.getElementById('shipperNotificationBody');
        if (!body) return;

        let html = '';

        notifications.forEach(notif => {
            // Xử lý icon và màu sắc theo loại thông báo
            let iconClass = 'fas fa-bell';
            let bgClass = 'icon-system';

            if (notif.type === 'NEW_ORDER') {
                // Đơn hàng mới trên sàn
                iconClass = 'fas fa-box-open';
                bgClass = 'icon-new-order';
            } else if (notif.type === 'ACCEPTED') {
                // Đã nhận đơn hàng
                iconClass = 'fas fa-shipping-fast';
                bgClass = 'icon-accepted';
            } else if (notif.type === 'COMPLETED') {
                // Đã giao thành công
                iconClass = 'fas fa-check-circle';
                bgClass = 'icon-completed';
            }

            const timeAgo = formatTimeAgo(notif.createdAt);
            const unreadClass = notif.read ? '' : 'unread';

            // Tạo HTML cho từng item
            html += `
                <div class="notification-item ${unreadClass}" data-id="${notif.id}" data-type="${notif.type}">
                    <div class="notification-item-icon ${bgClass}">
                        <i class="${iconClass}"></i>
                    </div>
                    <div class="notification-item-content">
                        <div class="notification-item-title">${escapeHtml(notif.title)}</div>
                        <div class="notification-item-message">${escapeHtml(notif.message)}</div>
                        <div class="notification-item-time">${timeAgo}</div>
                    </div>
                    ${!notif.read ? '<div class="notification-item-dot"></div>' : ''}
                </div>
            `;
        });

        body.innerHTML = html;

        // Bind click event - đánh dấu đã đọc và chuyển đến section tương ứng
        body.querySelectorAll('.notification-item').forEach(item => {
            item.addEventListener('click', function() {
                const notifId = this.dataset.id;
                const type = this.dataset.type;

                // Đánh dấu đã đọc (gọi API)
                markAsRead(notifId, this);

                // Đóng dropdown
                document.getElementById('shipperNotificationDropdown').classList.remove('show');

                // Chuyển đến section phù hợp
                if (type === 'NEW_ORDER') {
                    showSection('orders'); // Sàn đơn hàng
                } else if (type === 'ACCEPTED') {
                    showSection('overview'); // Tổng quan - đơn đang giao
                } else if (type === 'COMPLETED') {
                    showSection('history'); // Lịch sử
                }
            });
        });
    }

    // Đánh dấu một thông báo đã đọc
    function markAsRead(notificationId, element) {
        fetch(`${contextPath}/api/shipper/notifications`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: `action=markRead&notificationId=${encodeURIComponent(notificationId)}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Cập nhật UI - bỏ class unread
                if (element) {
                    element.classList.remove('unread');
                    const dot = element.querySelector('.notification-item-dot');
                    if (dot) dot.remove();
                }

                // Cập nhật badge số đếm
                updateBadge(data.unreadCount);
            }
        })
        .catch(error => {
            console.error('Error marking notification as read:', error);
        });
    }

    // Lấy icon theo loại thông báo
    function getNotificationIcon(type) {
        const icons = {
            'shipper_order': 'fas fa-box text-warning',
            'shipper_accepted': 'fas fa-check-circle text-success',
            'shipper_completed': 'fas fa-trophy text-primary',
            'order': 'fas fa-shopping-bag text-info',
            'system': 'fas fa-cog text-secondary',
            'promotion': 'fas fa-tag text-danger'
        };
        return icons[type] || 'fas fa-bell text-muted';
    }

    // Hàm helper: Tính thời gian tương đối (Vừa xong, 5 phút trước...)
    function formatTimeAgo(dateString) {
        const date = new Date(dateString);
        const now = new Date();
        const diffSeconds = Math.floor((now - date) / 1000);

        if (diffSeconds < 60) return 'Vừa xong';

        const diffMinutes = Math.floor(diffSeconds / 60);
        if (diffMinutes < 60) return `${diffMinutes} phút trước`;

        const diffHours = Math.floor(diffMinutes / 60);
        if (diffHours < 24) return `${diffHours} giờ trước`;

        const diffDays = Math.floor(diffHours / 24);
        if (diffDays < 7) return `${diffDays} ngày trước`;

        return date.toLocaleDateString('vi-VN'); // dd/mm/yyyy
    }

    // Escape HTML
    function escapeHtml(text) {
        if (!text) return "";
        return text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
    }

    // Cập nhật badge số thông báo
    function updateBadge(count) {
        const badge = document.getElementById('shipperNotificationBadge');
        const bell = document.querySelector('#shipperNotificationBell i');

        if (count > 0) {
            if (badge) {
                badge.textContent = count > 99 ? '99+' : count;
                badge.style.display = 'flex';
            }
            if (bell) {
                bell.classList.add('has-notifications');
            }
        } else {
            if (badge) {
                badge.style.display = 'none';
            }
            if (bell) {
                bell.classList.remove('has-notifications');
            }
        }
    }

    // Đánh dấu tất cả đã đọc
    function markAllAsRead() {
        fetch(`${contextPath}/api/shipper/notifications`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: 'action=markAllRead'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Cập nhật UI
                document.querySelectorAll('.notification-item.unread').forEach(item => {
                    item.classList.remove('unread');
                    const dot = item.querySelector('.notification-item-dot');
                    if (dot) dot.remove();
                });

                // Cập nhật badge với số mới từ server
                updateBadge(data.unreadCount);

                // Hiển thị toast 5 giây
                showToast('Đã đánh dấu tất cả đã đọc', 'success', 5000);
            }
        })
        .catch(error => {
            console.error('Error marking all as read:', error);
        });
    }

    // Mở modal xem tất cả thông báo
    function openNotificationModal() {
        // Đóng dropdown trước
        const dropdown = document.getElementById('shipperNotificationDropdown');
        if (dropdown) dropdown.classList.remove('show');

        // Tạo modal nếu chưa có
        let modal = document.getElementById('allNotificationsModal');
        if (!modal) {
            modal = createNotificationModal();
            document.body.appendChild(modal);
        }

        // Hiển thị modal
        modal.style.display = 'flex';
        loadAllNotifications();
    }

    // Tạo modal xem tất cả thông báo
    function createNotificationModal() {
        const modal = document.createElement('div');
        modal.id = 'allNotificationsModal';
        modal.className = 'notification-modal-backdrop';
        modal.innerHTML = `
            <div class="notification-modal">
                <div class="notification-modal-header">
                    <h3><i class="fas fa-bell"></i> Tất cả thông báo</h3>
                    <button class="btn-close-modal" onclick="document.getElementById('allNotificationsModal').style.display='none'">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="notification-modal-body" id="allNotificationsBody">
                    <div class="notification-loading">
                        <i class="fas fa-spinner fa-spin"></i>
                        <p>Đang tải...</p>
                    </div>
                </div>
            </div>
        `;

        // Đóng khi click backdrop
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });

        return modal;
    }

    // Load tất cả thông báo cho modal
    function loadAllNotifications() {
        const body = document.getElementById('allNotificationsBody');
        if (!body) return;

        fetch(`${contextPath}/api/notifications/recent`)
            .then(response => response.json())
            .then(data => {
                if (data.success && data.notifications && data.notifications.length > 0) {
                    let html = '';
                    data.notifications.forEach(notif => {
                        const iconClass = getNotificationIcon(notif.type);
                        const timeAgo = formatTimeAgo(notif.createdAt);
                        const unreadClass = notif.read ? '' : 'unread';

                        html += `
                            <div class="notification-modal-item ${unreadClass}" data-id="${notif.notificationId}">
                                <div class="notification-item-icon">
                                    <i class="${iconClass}"></i>
                                </div>
                                <div class="notification-item-content">
                                    <div class="notification-item-title">${escapeHtml(notif.title)}</div>
                                    <div class="notification-item-message">${escapeHtml(notif.message)}</div>
                                    <div class="notification-item-time">${timeAgo}</div>
                                </div>
                                ${notif.read ? '' : '<div class="notification-item-dot"></div>'}
                            </div>
                        `;
                    });
                    body.innerHTML = html;

                    // Bind events
                    body.querySelectorAll('.notification-modal-item').forEach(item => {
                        item.addEventListener('click', function() {
                            const id = this.dataset.id;
                            markAsRead(id);
                            this.classList.remove('unread');
                            const dot = this.querySelector('.notification-item-dot');
                            if (dot) dot.remove();
                        });
                    });
                } else {
                    body.innerHTML = `
                        <div class="notification-empty">
                            <i class="fas fa-bell-slash"></i>
                            <p>Bạn chưa có thông báo nào</p>
                        </div>
                    `;
                }
            })
            .catch(error => {
                console.error('Error loading all notifications:', error);
                body.innerHTML = `
                    <div class="notification-error">
                        <i class="fas fa-exclamation-triangle"></i>
                        <p>Không thể tải thông báo</p>
                    </div>
                `;
            });
    }

    // Hiển thị toast thông báo
    // duration: thời gian hiển thị (mặc định 5000ms = 5 giây)
    function showToast(message, type, duration) {
        type = type || 'info';
        duration = duration || 5000; // Mặc định 5 giây

        // Tạo container nếu chưa có
        let container = document.getElementById('toastContainer');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toastContainer';
            container.className = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = 'toast toast-' + type;

        let iconClass = 'fa-info-circle';
        if (type === 'success') iconClass = 'fa-check-circle';
        else if (type === 'error') iconClass = 'fa-times-circle';
        else if (type === 'warning') iconClass = 'fa-exclamation-triangle';

        toast.innerHTML = '<i class="fas ' + iconClass + '"></i><span>' + message + '</span>';

        container.appendChild(toast);

        // Animation
        setTimeout(function() { toast.classList.add('show'); }, 10);

        // Tự động ẩn sau duration (mặc định 5 giây)
        setTimeout(function() {
            toast.classList.remove('show');
            setTimeout(function() { toast.remove(); }, 300);
        }, duration);
    }

    // Polling kiểm tra thông báo mới (đơn mới trên sàn)
    let pollTimer = null;
    let lastUnreadCount = -1; // -1 để không hiện toast lần đầu tiên

    function startPolling() {
        if (pollTimer) return;

        pollTimer = setInterval(function() {
            fetch(`${contextPath}/api/shipper/notifications`)
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        const newCount = data.unreadCount;

                        // Cập nhật badge
                        updateBadge(newCount);

                        // Nếu có thông báo mới (so với lần trước)
                        if (newCount > lastUnreadCount && lastUnreadCount >= 0) {
                            // Reload notifications nếu dropdown đang mở
                            const dropdown = document.getElementById('shipperNotificationDropdown');
                            if (dropdown && dropdown.classList.contains('show')) {
                                loadNotifications();
                            }

                            // Hiển thị toast 5 giây
                            const diff = newCount - lastUnreadCount;
                            showToast(`🆕 Có ${diff} thông báo mới!`, 'warning', 5000);

                            // Play sound (optional)
                            if (CONFIG.soundEnabled) {
                                playNotificationSound();
                            }

                            // Rung chuông animation
                            const bell = document.querySelector('#shipperNotificationBell i');
                            if (bell) {
                                bell.classList.add('ringing');
                                setTimeout(function() { bell.classList.remove('ringing'); }, 1000);
                            }
                        }

                        lastUnreadCount = newCount;
                    }
                })
                .catch(function(error) {
                    console.error('Polling error:', error);
                });
        }, CONFIG.pollInterval);
    }

    // Play notification sound
    function playNotificationSound() {
        try {
            const audio = new Audio(contextPath + '/assets/sounds/notification.mp3');
            audio.volume = 0.3;
            audio.play().catch(function(e) { console.log('Cannot play sound:', e); });
        } catch (e) {
            console.log('Audio not supported');
        }
    }

    // Dừng polling
    function stopPolling() {
        if (pollTimer) {
            clearInterval(pollTimer);
            pollTimer = null;
        }
    }

    // Export functions để sử dụng từ bên ngoài
    window.ShipperNotification = {
        init: init,
        updateUnreadCount: updateUnreadCount,
        loadNotifications: loadNotifications,
        showToast: showToast,
        startPolling: startPolling,
        stopPolling: stopPolling
    };

    // Tự động khởi tạo khi DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();


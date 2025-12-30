// ===== XÓA HOÀN TOÀN ALERT CONTAINER KHI ĐÓNG =====
document.addEventListener('DOMContentLoaded', function() {
    // Lắng nghe sự kiện đóng alert
    const alerts = document.querySelectorAll('.alert-dismissible');

    alerts.forEach(function(alert) {
        alert.addEventListener('closed.bs.alert', function() {
            // Lấy container cha (.alert-container)
            const container = this.closest('.alert-container');

            // Xóa luôn container để không còn khoảng trắng
            if (container) {
                container.remove();
            }
        });
    });
});

// ===== HÀM MỞ MODAL KHUYẾN KHÍCH ĐĂNG NHẬP =====
function showLoginModal() {
    const loginModalElement = document.getElementById('loginModal');
    if (loginModalElement) {
        const myModal = new bootstrap.Modal(loginModalElement);
        myModal.show();
    }
}

// Hàm cập nhật badge thông báo (giữ nguyên)
function updateNotificationBadge() {
    if (!window.isLoggedIn) return;

    fetch(`${contextPath}/api/notifications/unread-count`)
        .then(response => response.json())
        .then(data => {
            const badge = document.querySelector('.notification-badge');
            if (data.count > 0) {
                if (!badge) {
                    // Tạo badge nếu chưa có
                    const bell = document.querySelector('.notification-bell');
                    if (bell) {
                        const newBadge = document.createElement('span');
                        newBadge.className = 'notification-badge new';
                        newBadge.textContent = data.count;
                        bell.appendChild(newBadge);
                    }
                } else {
                    badge.textContent = data.count;
                    badge.classList.add('new');
                    setTimeout(() => badge.classList.remove('new'), 1500);
                }
            } else if (badge) {
                badge.remove();
            }
        })
        .catch(error => console.error('Error updating notification badge:', error));
}

// Cập nhật mỗi 30 giây
if (window.isLoggedIn) {
    updateNotificationBadge();
    setInterval(updateNotificationBadge, 30000);
}

// ===== HIỆU ỨNG HEADER KHI CUỘN =====
document.addEventListener('DOMContentLoaded', function() {
    const navbar = document.querySelector('.navbar');

    if (navbar) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }
});
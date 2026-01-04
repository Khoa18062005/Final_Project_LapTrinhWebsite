// main.js - FIXED VERSION
document.addEventListener('DOMContentLoaded', function() {
    // ===== 1. XỬ LÝ ALERTS KHÔNG TỰ ĐỘNG ĐÓNG =====
    // Vô hiệu hóa auto-hide mặc định của Bootstrap
    const alerts = document.querySelectorAll('.alert');

    alerts.forEach(function(alert) {
        // Xóa class 'fade' và 'show' mặc định của Bootstrap
        alert.classList.remove('fade');
        alert.classList.add('show');

        // Ngăn Bootstrap tự động đóng alert
        const alertInstance = bootstrap.Alert.getOrCreateInstance(alert);
        if (alertInstance) {
            // Override phương thức đóng
            alertInstance._config.autohide = false;
            alertInstance._config.delay = 15000; // 15 giây
        }
    });

    // ===== 2. TỰ ĐỘNG ĐÓNG SAU 15s (TÙY CHỈNH) =====
    const autoHideAlerts = document.querySelectorAll('.alert.auto-hide');

    autoHideAlerts.forEach(function(alert) {
        // Chỉ đóng sau 15 giây
        setTimeout(function() {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 15000); // 15000ms = 15s
    });

    // ===== 3. XÓA CONTAINER KHI ALERT BỊ ĐÓNG =====
    document.addEventListener('closed.bs.alert', function(event) {
        const alertElement = event.target;
        const container = alertElement.closest('.alert-container');

        if (container) {
            // Thêm hiệu ứng fade out trước khi xóa
            container.style.transition = 'opacity 0.3s';
            container.style.opacity = '0';

            setTimeout(function() {
                if (container.parentNode) {
                    container.remove();
                }
            }, 300);
        }
    });

    // ===== 4. HIỆU ỨNG HEADER KHI CUỘN =====
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

    // ===== 5. CẬP NHẬT THÔNG BÁO (GIỮ NGUYÊN) =====
    function updateNotificationBadge() {
        if (!window.isLoggedIn) return;

        fetch(`${contextPath}/api/notifications/unread-count`)
            .then(response => response.json())
            .then(data => {
                const badge = document.querySelector('.notification-badge');
                if (data.count > 0) {
                    if (!badge) {
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

    // Cập nhật thông báo mỗi 30s nếu đã đăng nhập
    if (window.isLoggedIn) {
        updateNotificationBadge();
        setInterval(updateNotificationBadge, 30000);
    }

    // ===== 6. TẮT ANIMATION COUNTDOWN TRONG CSS (nếu không muốn) =====
    // Thêm style để tạm thời vô hiệu hóa thanh đếm ngược
    const style = document.createElement('style');
    style.textContent = `
        .alert::after {
            display: none !important;
        }
    `;
    document.head.appendChild(style);

    // ===== 7. ĐỒNG BỘ WIDTH DROPDOWN (CÁCH 3 - NÂNG CẤP) =====
    function syncDropdownWidth() {
        // 1. Lấy TẤT CẢ các phần tử có class .items-header (thay vì chỉ 1 cái)
        const headerItems = document.querySelectorAll('.items-header');

        headerItems.forEach(item => {
            // Tìm menu con CỤ THỂ của từng item
            const dropdownMenu = item.querySelector('.dropdown-menu');

            if (dropdownMenu) {
                // Lấy chiều rộng hiện tại của thẻ cha
                const width = item.getBoundingClientRect().width; // Chính xác hơn offsetWidth

                // Gán chiều rộng cho menu con
                // Dùng style.cssText để gán đè mạnh mẽ hơn
                dropdownMenu.style.cssText = `
                    width: ${width}px !important;
                    min-width: ${width}px !important;
                `;
            }
        });
    }

    // GỌI HÀM TẠI CÁC THỜI ĐIỂM QUAN TRỌNG:

    // 1. Ngay khi tải xong
    syncDropdownWidth();

    // 2. Khi người dùng co kéo trình duyệt
    window.addEventListener('resize', syncDropdownWidth);

    // 3. (QUAN TRỌNG) Khi bấm vào nút dropdown (đề phòng lúc đầu bị ẩn JS tính sai)
    const dropdownToggles = document.querySelectorAll('.items-header .dropdown-toggle');
    dropdownToggles.forEach(btn => {
        btn.addEventListener('click', () => {
            // Chờ 1 chút để Bootstrap render class show rồi mới tính
            setTimeout(syncDropdownWidth, 0);
        });
    });
});
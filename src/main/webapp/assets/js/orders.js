// ===== ORDERS PAGE JAVASCRIPT =====

document.addEventListener('DOMContentLoaded', function() {
    // Get all tab buttons
    const tabButtons = document.querySelectorAll('.orders-tabs .nav-link');

    // Add click event to each tab button
    tabButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();

            // Get status from data attribute
            const status = this.getAttribute('data-status');

            // Remove active class from all buttons
            tabButtons.forEach(btn => btn.classList.remove('active'));

            // Add active class to clicked button
            this.classList.add('active');

            // Redirect to orders page with status parameter
            const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');

            // Construct URL with status parameter
            let url = contextPath + '/profile/orders';
            if (status && status !== 'all') {
                url += '?status=' + status;
            }

            // Redirect
            window.location.href = url;
        });
    });

    // Auto-activate tab based on URL parameter
    const urlParams = new URLSearchParams(window.location.search);
    const currentStatus = urlParams.get('status') || 'all';

    tabButtons.forEach(button => {
        const buttonStatus = button.getAttribute('data-status');
        if (buttonStatus === currentStatus) {
            button.classList.add('active');
        } else {
            button.classList.remove('active');
        }
    });
});

// ===== ORDER ACTIONS =====

// Hủy đơn hàng
function cancelOrder(orderId) {
    if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        // TODO: Gọi API hủy đơn hàng
        console.log('Hủy đơn hàng:', orderId);

        // Hiển thị loading
        showLoading();

        // Giả lập call API
        setTimeout(() => {
            hideLoading();
            showToast('Đơn hàng đã được hủy thành công', 'success');
            // Reload trang sau 1.5 giây
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        }, 1000);
    }
}

// Mua lại đơn hàng
function reorder(orderId) {
    // TODO: Gọi API thêm lại sản phẩm vào giỏ hàng
    console.log('Mua lại đơn hàng:', orderId);

    showLoading();

    setTimeout(() => {
        hideLoading();
        showToast('Đã thêm sản phẩm vào giỏ hàng', 'success');
        // Chuyển đến trang giỏ hàng
        setTimeout(() => {
            const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
            window.location.href = contextPath + '/cart';
        }, 1500);
    }, 1000);
}

// Đánh giá đơn hàng
function reviewOrder(orderId) {
    // TODO: Chuyển đến trang đánh giá
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');
    window.location.href = contextPath + '/profile/orders/' + orderId + '/review';
}

// ===== UTILITY FUNCTIONS =====

// Show loading overlay
function showLoading() {
    // Tạo loading overlay nếu chưa có
    if (!document.querySelector('.loading-overlay')) {
        const overlay = document.createElement('div');
        overlay.className = 'loading-overlay';
        overlay.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Đang xử lý...</span>
            </div>
        `;
        document.body.appendChild(overlay);

        // Add CSS for loading overlay
        const style = document.createElement('style');
        style.textContent = `
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 9999;
            }
            .loading-overlay .spinner-border {
                width: 3rem;
                height: 3rem;
            }
        `;
        document.head.appendChild(style);
    }

    document.querySelector('.loading-overlay').style.display = 'flex';
}

// Hide loading overlay
function hideLoading() {
    const overlay = document.querySelector('.loading-overlay');
    if (overlay) {
        overlay.style.display = 'none';
    }
}

// Show toast notification
function showToast(message, type = 'info') {
    // Remove existing toast if any
    const existingToast = document.querySelector('.toast-notification');
    if (existingToast) {
        existingToast.remove();
    }

    // Create toast
    const toast = document.createElement('div');
    toast.className = `toast-notification toast-${type}`;

    // Icon based on type
    let icon = '';
    switch(type) {
        case 'success':
            icon = '<i class="bi bi-check-circle-fill"></i>';
            break;
        case 'error':
            icon = '<i class="bi bi-x-circle-fill"></i>';
            break;
        case 'warning':
            icon = '<i class="bi bi-exclamation-triangle-fill"></i>';
            break;
        default:
            icon = '<i class="bi bi-info-circle-fill"></i>';
    }

    toast.innerHTML = `
        ${icon}
        <span>${message}</span>
    `;

    document.body.appendChild(toast);

    // Add CSS for toast
    if (!document.querySelector('#toast-styles')) {
        const style = document.createElement('style');
        style.id = 'toast-styles';
        style.textContent = `
            .toast-notification {
                position: fixed;
                top: 80px;
                right: 20px;
                background: white;
                padding: 16px 24px;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                display: flex;
                align-items: center;
                gap: 12px;
                z-index: 9998;
                font-weight: 600;
                animation: slideInRight 0.3s ease;
            }
            
            .toast-notification i {
                font-size: 1.25rem;
            }
            
            .toast-success {
                border-left: 4px solid #28a745;
            }
            
            .toast-success i {
                color: #28a745;
            }
            
            .toast-error {
                border-left: 4px solid #dc3545;
            }
            
            .toast-error i {
                color: #dc3545;
            }
            
            .toast-warning {
                border-left: 4px solid #ffc107;
            }
            
            .toast-warning i {
                color: #ffc107;
            }
            
            .toast-info {
                border-left: 4px solid #0d6efd;
            }
            
            .toast-info i {
                color: #0d6efd;
            }
            
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
            
            @media (max-width: 576px) {
                .toast-notification {
                    right: 10px;
                    left: 10px;
                    padding: 12px 16px;
                    font-size: 0.9rem;
                }
            }
        `;
        document.head.appendChild(style);
    }

    // Auto remove after 3 seconds
    setTimeout(() => {
        toast.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);

    // Add slideOut animation
    if (!document.querySelector('#slideout-animation')) {
        const style = document.createElement('style');
        style.id = 'slideout-animation';
        style.textContent = `
            @keyframes slideOutRight {
                from {
                    transform: translateX(0);
                    opacity: 1;
                }
                to {
                    transform: translateX(100%);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);
    }
}
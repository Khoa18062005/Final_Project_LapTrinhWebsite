/**
 * Cart Notification Handler
 * Xử lý thông báo khi thêm sản phẩm vào giỏ hàng
 */

document.addEventListener('DOMContentLoaded', function() {
    initCartNotification();
});

function initCartNotification() {
    const addToCartForm = document.getElementById('add-to-cart-form');

    if (addToCartForm) {
        addToCartForm.addEventListener('submit', handleAddToCart);
    }
}

function handleAddToCart(e) {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);

    // Kiểm tra variant nếu có
    const variantId = document.getElementById('selected-variant-id');
    if (variantId && !variantId.value && window.variantData) {
        showToast('Vui lòng chọn phiên bản sản phẩm!', 'warning');
        return;
    }

    // Gửi request bằng AJAX
    fetch(form.action, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                showToast('Đã thêm sản phẩm vào giỏ hàng!', 'success');
                updateCartCount();
            } else if (response.status === 401) {
                showToast('Vui lòng đăng nhập để thêm vào giỏ hàng!', 'warning');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 2000);
            } else {
                showToast('Có lỗi xảy ra. Vui lòng thử lại!', 'danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra. Vui lòng thử lại!', 'danger');
        });
}

function showToast(message, type = 'success') {
    const toastEl = document.getElementById('cartToast');
    if (!toastEl) {
        console.error('Toast element not found');
        return;
    }

    const toastMessage = document.getElementById('toast-message');
    const toastHeader = toastEl.querySelector('.toast-header');

    // Cập nhật icon dựa trên type
    const iconHtml = getToastIcon(type);
    const iconElement = toastHeader.querySelector('i');
    if (iconElement) {
        iconElement.className = iconHtml;
    }

    // Cập nhật nội dung
    toastMessage.textContent = message;

    // Cập nhật màu sắc
    toastHeader.className = `toast-header bg-${type} text-white`;

    // Hiển thị toast
    const toast = new bootstrap.Toast(toastEl, {
        animation: true,
        autohide: true,
        delay: 3000
    });
    toast.show();
}

function getToastIcon(type) {
    const icons = {
        'success': 'bi bi-check-circle-fill me-2',
        'danger': 'bi bi-exclamation-circle-fill me-2',
        'warning': 'bi bi-exclamation-triangle-fill me-2',
        'info': 'bi bi-info-circle-fill me-2'
    };
    return icons[type] || icons['success'];
}

function updateCartCount() {
    // Lấy context path từ element nếu có
    const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';

    fetch(`${contextPath}/cart/count`)
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch cart count');
            return response.json();
        })
        .then(data => {
            const cartBadge = document.querySelector('.cart-count');
            if (cartBadge && data.count !== undefined) {
                cartBadge.textContent = data.count;

                // Animation cho badge
                cartBadge.classList.add('badge-animate');
                setTimeout(() => {
                    cartBadge.classList.remove('badge-animate');
                }, 300);
            }
        })
        .catch(error => console.error('Error updating cart count:', error));
}

// Export functions nếu cần sử dụng ở nơi khác
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        showToast,
        updateCartCount
    };
}
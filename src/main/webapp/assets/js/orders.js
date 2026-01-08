// ===== ORDERS PAGE JAVASCRIPT - FINAL VERSION =====

document.addEventListener('DOMContentLoaded', function() {
    console.log('🛒 Orders.js loaded');

    const metaTag = document.querySelector('meta[name="context-path"]');
    const contextPath = metaTag ? metaTag.content : '';

    initOrderTabs(contextPath);
});

/**
 * ✅ KHỞI TẠO TAB SWITCHING
 */
function initOrderTabs(contextPath) {
    const tabButtons = document.querySelectorAll('.orders-tabs .nav-link');

    tabButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const status = this.getAttribute('data-status');

            if (this.classList.contains('active')) {
                console.log('Tab already active');
                return;
            }

            tabButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            let url = contextPath + '/profile/orders';
            if (status && status !== 'all') {
                url += '?status=' + status;
            }

            window.location.href = url;
        });
    });
}

/**
 * ✅ THANH TOÁN ĐƠN HÀNG
 */
function payOrder(orderId) {
    const contextPath = document.querySelector('meta[name="context-path"]').content;
    window.location.href = contextPath + '/payment?orderId=' + orderId;
}

/**
 * ✅ HỦY ĐƠN HÀNG
 */
function cancelOrder(orderId) {
    if (!confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        return;
    }

    showLoading();

    // TODO: Call API
    setTimeout(() => {
        hideLoading();
        showToast('Đơn hàng đã được hủy thành công!', 'success');
        setTimeout(() => window.location.reload(), 1500);
    }, 1000);
}

/**
 * ✅ MUA LẠI - CHỈ REDIRECT ĐẾN /cart
 */
function reorder() {
    const contextPath = document.querySelector('meta[name="context-path"]').content;
    window.location.href = contextPath + '/cart';
}

/**
 * ✅ ĐÁNH GIÁ SẢN PHẨM - REDIRECT ĐẾN /product?id=X
 */
function reviewProduct(productId) {
    const contextPath = document.querySelector('meta[name="context-path"]').content;
    window.location.href = contextPath + '/product?id=' + productId;
}

/**
 * ✅ XEM CHI TIẾT ĐƠN HÀNG - HIỂN THỊ MODAL
 */
function viewOrderDetail(orderId) {
    console.log('Loading order detail:', orderId);

    const contextPath = document.querySelector('meta[name="context-path"]').content;
    const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
    const content = document.getElementById('orderDetailContent');

    // Show loading
    content.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-3 text-muted">Đang tải chi tiết đơn hàng...</p>
        </div>
    `;

    modal.show();

    // ✅ FETCH REAL DATA FROM API
    fetch(contextPath + '/profile/orders/api/' + orderId)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch order detail');
            }
            return response.json();
        })
        .then(data => {
            console.log('Order data:', data);
            content.innerHTML = buildOrderDetailHTML(data);
        })
        .catch(error => {
            console.error('Error fetching order detail:', error);
            content.innerHTML = `
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    Không thể tải chi tiết đơn hàng! Vui lòng thử lại.
                </div>
            `;
        });
}

/**
 * ✅ BUILD HTML CHO MODAL CHI TIẾT
 */
function buildOrderDetailHTML(order) {
    return `
        <div class="order-detail-modal">
            <!-- Order Info -->
            <div class="order-info-section mb-4">
                <h6 class="section-title">
                    <i class="bi bi-info-circle me-2"></i>Thông Tin Đơn Hàng
                </h6>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="label">Mã đơn hàng:</span>
                        <span class="value">${order.orderNumber}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">Ngày đặt:</span>
                        <span class="value">${formatDate(order.orderDate)}</span>
                    </div>
                    <div class="info-item">
                        <span class="label">Trạng thái:</span>
                        <span class="value">
                            <span class="badge ${getStatusBadgeClass(order.status)}">
                                ${getStatusText(order.status)}
                            </span>
                        </span>
                    </div>
                </div>
            </div>
            
            <!-- Products List -->
            <div class="products-section mb-4">
                <h6 class="section-title">
                    <i class="bi bi-box me-2"></i>Sản Phẩm
                </h6>
                ${order.items.map(item => `
                    <div class="product-item-detail">
                        <div class="product-image">
                            <img src="${item.productImage}" alt="${item.productName}"
                                 onerror="this.src='${document.querySelector('meta[name="context-path"]').content}/assets/images/no-image.png'">
                        </div>
                        <div class="product-info">
                            <h6 class="product-name">${item.productName}</h6>
                            ${item.variantInfo ? `<p class="product-variant text-muted">${item.variantInfo}</p>` : ''}
                            <p class="product-quantity mb-0">Số lượng: x${item.quantity}</p>
                        </div>
                        <div class="product-price">
                            ${formatCurrency(item.price)}
                        </div>
                    </div>
                `).join('')}
            </div>
            
            <!-- Payment Summary -->
            <div class="payment-summary">
                <h6 class="section-title">
                    <i class="bi bi-credit-card me-2"></i>Thanh Toán
                </h6>
                <div class="summary-row">
                    <span>Tạm tính:</span>
                    <span>${formatCurrency(order.subtotal)}</span>
                </div>
                <div class="summary-row">
                    <span>Phí vận chuyển:</span>
                    <span>${formatCurrency(order.shippingFee)}</span>
                </div>
                ${order.discount > 0 ? `
                <div class="summary-row">
                    <span>Giảm giá:</span>
                    <span class="text-danger">-${formatCurrency(order.discount)}</span>
                </div>
                ` : ''}
                ${order.voucherDiscount > 0 ? `
                <div class="summary-row">
                    <span>Giảm voucher:</span>
                    <span class="text-danger">-${formatCurrency(order.voucherDiscount)}</span>
                </div>
                ` : ''}
                <div class="summary-row summary-total">
                    <span>Tổng cộng:</span>
                    <span class="text-primary fw-bold">${formatCurrency(order.totalAmount)}</span>
                </div>
            </div>
            
            ${order.notes ? `
            <!-- Notes -->
            <div class="notes-section mt-4">
                <h6 class="section-title">
                    <i class="bi bi-sticky me-2"></i>Ghi Chú
                </h6>
                <p class="text-muted">${order.notes}</p>
            </div>
            ` : ''}
        </div>
    `;
}

/**
 * ✅ UTILITY FUNCTIONS
 */
function showLoading() {
    let overlay = document.getElementById('loadingOverlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'loadingOverlay';
        overlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.5); display: flex;
            align-items: center; justify-content: center; z-index: 9999;
        `;
        overlay.innerHTML = `
            <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;">
                <span class="visually-hidden">Loading...</span>
            </div>
        `;
        document.body.appendChild(overlay);
    }
    overlay.style.display = 'flex';
}

function hideLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) overlay.style.display = 'none';
}

function showToast(message, type = 'info') {
    const existingToast = document.querySelector('.order-toast');
    if (existingToast) existingToast.remove();

    const colors = {
        success: '#28a745',
        error: '#dc3545',
        warning: '#ffc107',
        info: '#0d6efd'
    };

    const icons = {
        success: 'bi-check-circle-fill',
        error: 'bi-x-circle-fill',
        warning: 'bi-exclamation-triangle-fill',
        info: 'bi-info-circle-fill'
    };

    const toast = document.createElement('div');
    toast.className = 'order-toast';
    toast.style.cssText = `
        position: fixed; top: 80px; right: 20px; background: white;
        padding: 16px 24px; border-radius: 12px;
        border-left: 4px solid ${colors[type]};
        box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        display: flex; align-items: center; gap: 12px;
        z-index: 9998; font-weight: 600; min-width: 300px;
        animation: slideInRight 0.3s ease;
    `;
    toast.innerHTML = `
        <i class="bi ${icons[type]}" style="font-size: 1.25rem; color: ${colors[type]};"></i>
        <span>${message}</span>
    `;

    document.body.appendChild(toast);

    if (!document.querySelector('#toast-animations')) {
        const style = document.createElement('style');
        style.id = 'toast-animations';
        style.textContent = `
            @keyframes slideInRight {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOutRight {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }

    setTimeout(() => {
        if (toast && toast.parentElement) {
            toast.style.animation = 'slideOutRight 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }
    }, 3000);
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN').format(amount) + 'đ';
}

function formatDate(date) {
    return new Intl.DateTimeFormat('vi-VN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    }).format(new Date(date));
}

function getStatusBadgeClass(status) {
    const classes = {
        pending: 'badge-warning',
        processing: 'badge-info',
        shipping: 'badge-primary',
        completed: 'badge-success',
        cancelled: 'badge-danger',
        returned: 'badge-secondary'
    };
    return classes[status] || 'badge-secondary';
}

function getStatusText(status) {
    const texts = {
        pending: 'Chờ thanh toán',
        processing: 'Đang xử lý',
        shipping: 'Đang giao hàng',
        completed: 'Hoàn thành',
        cancelled: 'Đã hủy',
        returned: 'Trả hàng'
    };
    return texts[status] || status;
}

console.log('✓ Orders.js loaded successfully');
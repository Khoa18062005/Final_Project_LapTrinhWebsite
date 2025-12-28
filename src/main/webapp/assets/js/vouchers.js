// ===== VOUCHER PAGE JAVASCRIPT =====

document.addEventListener('DOMContentLoaded', function() {
    console.log('🎫 Vouchers.js loaded');
});

/**
 * Hiển thị modal chi tiết voucher
 */
function showVoucherDetail(voucherId) {
    // Get voucher data from window (được set từ JSP)
    if (!window.voucherData || !Array.isArray(window.voucherData)) {
        console.error('Voucher data not found');
        return;
    }

    const voucher = window.voucherData.find(v => v.id === voucherId);
    if (!voucher) {
        console.error('Voucher not found with ID:', voucherId);
        return;
    }

    // Build discount text
    let discountText = '';
    if (voucher.discountPercent > 0) {
        discountText = `Giảm ${voucher.discountPercent}%`;
        if (voucher.maxDiscount > 0) {
            discountText += ` (Tối đa ${formatCurrency(voucher.maxDiscount)})`;
        }
    } else if (voucher.discountAmount > 0) {
        discountText = `Giảm ${formatCurrency(voucher.discountAmount)}`;
    }

    // Build HTML
    const html = `
        <div class="voucher-detail">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Mã Voucher</label>
                    <div class="d-flex align-items-center gap-2">
                        <h4 class="text-primary mb-0">${voucher.code}</h4>
                        <button class="btn btn-sm btn-outline-primary" onclick="copyCode('${voucher.code}')">
                            <i class="bi bi-clipboard"></i>
                        </button>
                    </div>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Trạng Thái</label>
                    <span class="badge ${voucher.isActive ? 'bg-success' : 'bg-danger'} fs-6">
                        ${voucher.isActive ? 'Đang Hoạt Động' : 'Không Khả Dụng'}
                    </span>
                </div>
            </div>

            <div class="mb-3">
                <label class="fw-bold text-muted d-block mb-1">Tên Ưu Đãi</label>
                <p class="mb-0">${escapeHtml(voucher.name)}</p>
            </div>

            <div class="mb-3">
                <label class="fw-bold text-muted d-block mb-1">Mô Tả</label>
                <p class="mb-0">${voucher.description ? escapeHtml(voucher.description) : 'Không có mô tả'}</p>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Giảm Giá</label>
                    <p class="mb-0 text-success fw-bold fs-5">${discountText}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Đơn Tối Thiểu</label>
                    <p class="mb-0 fw-semibold">${voucher.minOrderValue > 0 ? formatCurrency(voucher.minOrderValue) : 'Không yêu cầu'}</p>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Thời Gian Bắt Đầu</label>
                    <p class="mb-0"><i class="bi bi-calendar-event me-1 text-primary"></i>${voucher.startDate}</p>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Thời Gian Kết Thúc</label>
                    <p class="mb-0"><i class="bi bi-calendar-x me-1 text-danger"></i>${voucher.expiryDate}</p>
                </div>
            </div>

            <div class="mb-3">
                <label class="fw-bold text-muted d-block mb-1">Số Lượt Sử Dụng</label>
                <div class="progress" style="height: 25px;">
                    <div class="progress-bar ${getProgressBarColor(voucher.usageCount, voucher.usageLimit)}" 
                         role="progressbar"
                         style="width: ${(voucher.usageCount / voucher.usageLimit) * 100}%"
                         aria-valuenow="${voucher.usageCount}" 
                         aria-valuemin="0" 
                         aria-valuemax="${voucher.usageLimit}">
                        ${voucher.usageCount} / ${voucher.usageLimit}
                    </div>
                </div>
                <small class="text-muted mt-1 d-block">
                    Còn lại: <strong>${voucher.usageLimit - voucher.usageCount}</strong> lượt
                </small>
            </div>

            ${voucher.type ? `
                <div class="mb-3">
                    <label class="fw-bold text-muted d-block mb-1">Loại Voucher</label>
                    <span class="badge bg-info">${voucher.type}</span>
                </div>
            ` : ''}
        </div>
    `;

    // Update modal content
    document.getElementById('voucherDetailContent').innerHTML = html;

    // Show modal
    const modal = new bootstrap.Modal(document.getElementById('voucherDetailModal'));
    modal.show();
}

/**
 * Xác định màu progress bar dựa trên usage
 */
function getProgressBarColor(usageCount, usageLimit) {
    const percentage = (usageCount / usageLimit) * 100;
    if (percentage >= 90) return 'bg-danger';
    if (percentage >= 70) return 'bg-warning';
    return 'bg-success';
}

/**
 * Format currency VND
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

/**
 * Copy voucher code to clipboard
 */
function copyCode(code) {
    navigator.clipboard.writeText(code).then(() => {
        // Show success toast
        showToast('success', 'Đã sao chép mã: ' + code);
    }).catch(err => {
        console.error('Failed to copy:', err);
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = code;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        showToast('success', 'Đã sao chép mã: ' + code);
    });
}

/**
 * Show toast notification
 */
function showToast(type, message) {
    const existingToast = document.querySelector('.voucher-toast');
    if (existingToast) {
        existingToast.remove();
    }

    const toast = document.createElement('div');
    toast.className = `alert alert-${type === 'success' ? 'success' : 'danger'} alert-dismissible fade show voucher-toast`;
    toast.style.position = 'fixed';
    toast.style.top = '20px';
    toast.style.right = '20px';
    toast.style.zIndex = '9999';
    toast.style.minWidth = '300px';
    toast.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';

    const icon = type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill';

    toast.innerHTML = `
        <i class="bi ${icon} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(toast);

    // Auto remove after 3 seconds
    setTimeout(() => {
        if (toast && toast.parentElement) {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 150);
        }
    }, 3000);
}

/**
 * Escape HTML to prevent XSS
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}
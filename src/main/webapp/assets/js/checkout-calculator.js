/**
 * Checkout Calculator - Xử lý tính toán giá với Voucher, Loyalty Points & Phí ship
 * File: /assets/js/checkout-calculator.js
 * Update: Tự động hiển thị số tiền giảm voucher ngay khi chọn
 */

class CheckoutCalculator {
    constructor() {
        this.originalSubtotal = parseInt(orderTotal) || 0;
        this.shippingFee = 30000;

        this.appliedVoucher = null;
        this.voucherDiscount = 0;

        this.maxLoyaltyPoints = parseInt(document.querySelector('.loyalty-points-display')?.textContent || 0);
        this.usedLoyaltyPoints = 0;
        this.loyaltyDiscount = 0;
        this.POINT_VALUE = 1000;

        this.currentTotal = this.originalSubtotal + this.shippingFee;

        this.init();
    }

    init() {
        this.setupVoucherHandlers();
        this.setupLoyaltyHandlers();
        this.updateDisplay(); // Hiển thị ban đầu
    }

    // ==================== VOUCHER HANDLERS ====================
    setupVoucherHandlers() {
        const voucherCards = document.querySelectorAll('.voucher-card');
        const applyVoucherBtn = document.getElementById('applyVoucherBtn');
        const removeVoucherBtn = document.getElementById('removeVoucherBtn');
        const voucherCodeInput = document.getElementById('voucherCodeInput');

        // Click vào voucher card → áp dụng NGAY LẬP TỨC
        voucherCards.forEach(card => {
            card.addEventListener('click', () => {
                const voucherCode = card.dataset.voucherCode;
                const minOrder = parseFloat(card.dataset.minOrder || 0);

                if (this.originalSubtotal < minOrder) {
                    this.showNotification('Đơn hàng chưa đạt giá trị tối thiểu!', 'warning');
                    return;
                }

                this.applyVoucherFromCard(card);
            });
        });

        // Nút áp dụng khi nhập code thủ công
        if (applyVoucherBtn) {
            applyVoucherBtn.addEventListener('click', () => {
                const code = voucherCodeInput.value.trim();
                if (!code) {
                    this.showNotification('Vui lòng nhập mã voucher!', 'warning');
                    return;
                }
                this.applyVoucherByCode(code);
            });
        }

        if (removeVoucherBtn) {
            removeVoucherBtn.addEventListener('click', () => this.removeVoucher());
        }
    }

    applyVoucherFromCard(card) {
        // Bỏ chọn các card khác
        document.querySelectorAll('.voucher-card').forEach(c => c.classList.remove('selected'));
        card.classList.add('selected');

        // Lấy thông tin voucher
        this.appliedVoucher = {
            code: card.dataset.voucherCode,
            type: card.dataset.voucherType || 'PERCENTAGE',
            discountPercent: parseFloat(card.dataset.discountPercent || 0),
            discountAmount: parseFloat(card.dataset.discountAmount || 0),
            maxDiscount: parseFloat(card.dataset.maxDiscount || 0)
        };

        // Điền vào input và hiển thị phần applied
        document.getElementById('voucherCodeInput').value = this.appliedVoucher.code;
        document.getElementById('appliedVoucherCode').textContent = this.appliedVoucher.code;
        document.getElementById('appliedVoucherDisplay').classList.remove('d-none');

        this.calculateVoucherDiscount();
        this.recalculateTotal();
        this.showNotification(`Đã áp dụng voucher: -${this.formatCurrency(this.voucherDiscount)}`, 'success');
    }

    applyVoucherByCode(code) {
        const voucherCard = Array.from(document.querySelectorAll('.voucher-card'))
            .find(card => card.dataset.voucherCode === code);

        if (voucherCard) {
            this.applyVoucherFromCard(voucherCard);
        } else {
            this.showNotification('Mã voucher không hợp lệ hoặc không áp dụng được!', 'error');
        }
    }

    calculateVoucherDiscount() {
        if (!this.appliedVoucher) {
            this.voucherDiscount = 0;
            return;
        }

        let discount = 0;
        const subtotal = this.originalSubtotal;

        if (this.appliedVoucher.type === 'PERCENTAGE') {
            discount = subtotal * (this.appliedVoucher.discountPercent / 100);
            if (this.appliedVoucher.maxDiscount > 0) {
                discount = Math.min(discount, this.appliedVoucher.maxDiscount);
            }
        } else if (this.appliedVoucher.type === 'FIXED_AMOUNT') {
            discount = this.appliedVoucher.discountAmount;
        } else if (this.appliedVoucher.type === 'SHIPPING') {
            discount = this.appliedVoucher.discountAmount;
        }

        this.voucherDiscount = Math.min(discount, subtotal);
    }

    removeVoucher() {
        this.appliedVoucher = null;
        this.voucherDiscount = 0;

        document.getElementById('voucherCodeInput').value = '';
        document.getElementById('appliedVoucherDisplay').classList.add('d-none');
        document.querySelectorAll('.voucher-card').forEach(c => c.classList.remove('selected'));

        this.recalculateTotal();
        this.showNotification('Đã xóa voucher', 'info');
    }

    // ==================== LOYALTY POINTS (giữ nguyên logic, chỉ tinh chỉnh nhỏ) ====================
    setupLoyaltyHandlers() {
        const loyaltyInput = document.getElementById('loyaltyPointsInput');
        const applyLoyaltyBtn = document.getElementById('applyLoyaltyBtn');
        const useAllLoyaltyBtn = document.getElementById('useAllLoyaltyBtn');
        const removeLoyaltyBtn = document.getElementById('removeLoyaltyBtn');

        if (applyLoyaltyBtn) {
            applyLoyaltyBtn.addEventListener('click', () => {
                const points = parseInt(loyaltyInput.value) || 0;
                this.applyLoyaltyPoints(points);
            });
        }

        if (useAllLoyaltyBtn) {
            useAllLoyaltyBtn.addEventListener('click', () => this.useAllLoyaltyPoints());
        }

        if (removeLoyaltyBtn) {
            removeLoyaltyBtn.addEventListener('click', () => this.removeLoyaltyPoints());
        }

        if (loyaltyInput) {
            loyaltyInput.addEventListener('input', () => {
                let value = parseInt(loyaltyInput.value) || 0;
                value = Math.max(0, Math.min(value, this.maxLoyaltyPoints));
                loyaltyInput.value = value;
            });
        }
    }

    applyLoyaltyPoints(points) {
        if (points <= 0) return;

        points = Math.min(points, this.maxLoyaltyPoints);

        const amountAfterVoucher = this.originalSubtotal - this.voucherDiscount + this.shippingFee;
        const maxPoints = Math.floor(amountAfterVoucher / this.POINT_VALUE);

        if (points > maxPoints) {
            points = maxPoints;
            this.showNotification(`Tối đa chỉ dùng được ${points} điểm!`, 'warning');
        }

        this.usedLoyaltyPoints = points;
        this.loyaltyDiscount = points * this.POINT_VALUE;

        document.getElementById('loyaltyPointsInput').value = points;
        document.getElementById('appliedLoyaltyPoints').textContent = points;
        document.getElementById('appliedLoyaltyValue').textContent = this.formatCurrency(this.loyaltyDiscount);
        document.getElementById('appliedLoyaltyDisplay').classList.remove('d-none');
        document.getElementById('usedLoyaltyPoints').value = points;

        this.recalculateTotal();
        this.showNotification(`Đã áp dụng ${points} điểm (-${this.formatCurrency(this.loyaltyDiscount)})`, 'success');
    }

    useAllLoyaltyPoints() {
        const amountAfterVoucher = this.originalSubtotal - this.voucherDiscount + this.shippingFee;
        const maxPoints = Math.min(this.maxLoyaltyPoints, Math.floor(amountAfterVoucher / this.POINT_VALUE));

        if (maxPoints > 0) {
            document.getElementById('loyaltyPointsInput').value = maxPoints;
            this.applyLoyaltyPoints(maxPoints);
        } else {
            this.showNotification('Không thể sử dụng điểm cho đơn này!', 'warning');
        }
    }

    removeLoyaltyPoints() {
        this.usedLoyaltyPoints = 0;
        this.loyaltyDiscount = 0;

        document.getElementById('loyaltyPointsInput').value = 0;
        document.getElementById('appliedLoyaltyDisplay').classList.add('d-none');
        document.getElementById('usedLoyaltyPoints').value = 0;

        this.recalculateTotal();
        this.showNotification('Đã xóa điểm tích lũy', 'info');
    }

    // ==================== TÍNH TOÁN & HIỂN THỊ ====================
    recalculateTotal() {
        this.calculateVoucherDiscount();

        let intermediate = this.originalSubtotal - this.voucherDiscount + this.shippingFee;
        this.currentTotal = Math.max(0, intermediate - this.loyaltyDiscount);

        this.updateDisplay();
    }

    updateDisplay() {
        const totalElement = document.querySelector('.total-value');
        if (totalElement) {
            totalElement.textContent = this.formatCurrency(this.currentTotal);
        }
        this.updateBreakdown();
    }

    updateBreakdown() {
        let html = `
            <div class="price-breakdown mt-3 p-3 bg-light rounded border">
                <div class="d-flex justify-content-between mb-2">
                    <span>Tổng tiền hàng:</span>
                    <span>${this.formatCurrency(this.originalSubtotal)}</span>
                </div>`;

        if (this.voucherDiscount > 0) {
            html += `
                <div class="d-flex justify-content-between mb-2 text-success">
                    <span>Giảm voucher:</span>
                    <span class="fw-bold">-${this.formatCurrency(this.voucherDiscount)}</span>
                </div>`;
        }

        html += `
                <div class="d-flex justify-content-between mb-2">
                    <span>Phí vận chuyển:</span>
                    <span>${this.formatCurrency(this.shippingFee)}</span>
                </div>`;

        if (this.loyaltyDiscount > 0) {
            html += `
                <div class="d-flex justify-content-between mb-2 text-warning">
                    <span>Giảm điểm tích lũy:</span>
                    <span class="fw-bold">-${this.formatCurrency(this.loyaltyDiscount)}</span>
                </div>`;
        }

        html += `
                <div class="d-flex justify-content-between pt-3 border-top fw-bold fs-5">
                    <span>Thành tiền:</span>
                    <span class="text-danger">${this.formatCurrency(this.currentTotal)}</span>
                </div>
            </div>`;

        const totalSection = document.querySelector('.total-section');
        if (totalSection) {
            const existing = totalSection.querySelector('.price-breakdown');
            if (existing) existing.outerHTML = html;
            else totalSection.insertAdjacentHTML('beforeend', html);
        }
    }

    formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        }).format(Math.round(amount));
    }

    showNotification(message, type = 'info') {
        const notif = document.createElement('div');
        notif.className = `alert alert-${type} position-fixed top-0 end-0 m-4 shadow-lg`;
        notif.style.zIndex = '2000';
        notif.innerHTML = `<i class="bi ${this.getIcon(type)} me-2"></i>${message}`;
        document.body.appendChild(notif);

        setTimeout(() => notif.classList.add('show'), 10);
        setTimeout(() => {
            notif.classList.remove('show');
            setTimeout(() => notif.remove(), 400);
        }, 2800);
    }

    getIcon(type) {
        const icons = {
            success: 'bi-check-circle-fill',
            error: 'bi-x-circle-fill',
            warning: 'bi-exclamation-triangle-fill',
            info: 'bi-info-circle-fill'
        };
        return icons[type] || 'bi-info-circle';
    }
}

// Khởi tạo
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('loyaltyPointsInput')) {
        window.checkoutCalculator = new CheckoutCalculator();
    }
});
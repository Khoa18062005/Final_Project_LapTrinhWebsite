/**
 * Cart Controller JavaScript
 * Xử lý tất cả logic cho trang giỏ hàng
 */

const cartController = {

    // Khởi tạo
    init: function() {
        this.bindEvents();
        this.initializeCartRows();
        this.updateTotal();
    },

    // Gắn sự kiện
    bindEvents: function() {
        // Chọn/bỏ chọn tất cả
        const selectAll = document.getElementById('select-all');
        if (selectAll) {
            selectAll.addEventListener('change', this.handleSelectAll.bind(this));
        }

        // Gắn sự kiện cho các checkbox item
        document.querySelectorAll('.item-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', this.handleItemCheckboxChange.bind(this));
        });

        // Áp dụng voucher
        const applyVoucherBtn = document.getElementById('apply-voucher');
        if (applyVoucherBtn) {
            applyVoucherBtn.addEventListener('click', this.applyVoucher.bind(this));
        }

        // Enter để áp dụng voucher
        const voucherInput = document.getElementById('voucher-code');
        if (voucherInput) {
            voucherInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.applyVoucher();
                }
            });
        }
    },

    // Khởi tạo data attributes cho các dòng sản phẩm
    initializeCartRows: function() {
        document.querySelectorAll('.cart-item-row').forEach(row => {
            const checkbox = row.querySelector('.item-checkbox');
            if (checkbox) {
                row.dataset.productId = checkbox.value;
                row.dataset.variantId = checkbox.dataset.variantId;
            }
        });
    },

    // Giảm số lượng
    decreaseQuantity: function(productId, variantId) {
        const input = document.querySelector(`input[data-product-id="${productId}"][data-variant-id="${variantId}"]`);
        if (!input) return;

        let newQuantity = parseInt(input.value) - 1;
        if (newQuantity < 1) {
            this.showRemoveConfirm(productId, variantId);
            return;
        }

        this.updateQuantity(productId, variantId, newQuantity);
    },

    // Tăng số lượng
    increaseQuantity: function(productId, variantId) {
        const input = document.querySelector(`input[data-product-id="${productId}"][data-variant-id="${variantId}"]`);
        if (!input) return;

        let newQuantity = parseInt(input.value) + 1;
        if (newQuantity > 99) {
            newQuantity = 99;
        }

        this.updateQuantity(productId, variantId, newQuantity);
    },

    // Cập nhật số lượng từ input
    updateQuantityFromInput: function(input) {
        const productId = input.dataset.productId;
        const variantId = input.dataset.variantId;
        const newQuantity = parseInt(input.value) || 1;

        this.updateQuantity(productId, variantId, newQuantity);
    },

    // Cập nhật số lượng (gửi request)
    updateQuantity: function(productId, variantId, newQuantity) {
        const url = `${window.contextPath}/cart`;

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=update&productId=${productId}&variantId=${variantId}&quantity=${newQuantity}`
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    // Cập nhật UI ngay lập tức
                    const input = document.querySelector(`input[data-product-id="${productId}"][data-variant-id="${variantId}"]`);
                    if (input) {
                        input.value = newQuantity;
                        this.updateItemSubtotal(productId, variantId, newQuantity);
                        this.updateTotal();
                    }
                }
            })
            .catch(error => console.error('Error:', error));
    },

    // Xóa sản phẩm
    removeItem: function(productId, variantId) {
        this.showRemoveConfirm(productId, variantId, false);
    },

    // Hiển thị modal xác nhận xóa
    showRemoveConfirm: function(productId, variantId, isClearAll = false) {
        const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
        const confirmBtn = document.getElementById('confirm-action');
        const message = document.getElementById('modal-message');

        if (isClearAll) {
            message.textContent = 'Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?';
        } else {
            message.textContent = 'Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?';
        }

        confirmBtn.onclick = () => {
            if (isClearAll) {
                this.clearCart();
            } else {
                this.confirmRemoveItem(productId, variantId);
            }
            modal.hide();
        };

        modal.show();
    },

    // Xác nhận xóa item
    confirmRemoveItem: function(productId, variantId) {
        const url = `${window.contextPath}/cart`;

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=remove&productId=${productId}&variantId=${variantId}`
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    // Xóa khỏi UI
                    const row = document.querySelector(`tr[data-product-id="${productId}"][data-variant-id="${variantId}"]`);
                    if (row) {
                        row.remove();
                        this.updateTotal();
                        this.updateCartCount(this.getCartCount() - 1);
                    }
                }
            })
            .catch(error => console.error('Error:', error));
    },

    // Xóa toàn bộ giỏ hàng
    confirmClearCart: function() {
        this.showRemoveConfirm(null, null, true);
    },

    clearCart: function() {
        const url = `${window.contextPath}/cart`;

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'action=clear'
        })
            .then(response => {
                window.location.href = `${window.contextPath}/cart`;
            })
            .catch(error => console.error('Error:', error));
    },

    // Cập nhật tổng tiền của từng item
    updateItemSubtotal: function(productId, variantId, quantity) {
        const row = document.querySelector(`tr[data-product-id="${productId}"][data-variant-id="${variantId}"]`);
        if (!row) return;

        const priceElement = row.querySelector('.item-price');
        const subtotalElement = row.querySelector('.item-subtotal');

        if (priceElement && subtotalElement) {
            const priceText = priceElement.textContent.replace(/[^\d]/g, '');
            const price = parseInt(priceText) || 0;
            const subtotal = price * quantity;

            subtotalElement.textContent = this.formatCurrency(subtotal);
        }
    },

    // Cập nhật tổng tiền
    updateTotal: function() {
        let subtotal = 0;
        const checkboxes = document.querySelectorAll('.item-checkbox:checked');

        checkboxes.forEach(checkbox => {
            const productId = checkbox.value;
            const variantId = checkbox.dataset.variantId;
            const row = document.querySelector(`tr[data-product-id="${productId}"][data-variant-id="${variantId}"]`);

            if (row) {
                const priceElement = row.querySelector('.item-price');
                const quantityInput = row.querySelector('.quantity-input');

                if (priceElement && quantityInput) {
                    const priceText = priceElement.textContent.replace(/[^\d]/g, '');
                    const price = parseInt(priceText) || 0;
                    const quantity = parseInt(quantityInput.value) || 0;

                    subtotal += price * quantity;
                }
            }
        });

        // Cập nhật UI
        const subtotalElement = document.getElementById('subtotal-amount');
        const totalElement = document.getElementById('total-amount');

        if (subtotalElement) {
            subtotalElement.textContent = this.formatCurrency(subtotal);
        }

        if (totalElement) {
            totalElement.textContent = this.formatCurrency(subtotal);
        }

        // Kiểm tra nếu không có sản phẩm nào được chọn
        const checkoutBtn = document.getElementById('checkout-btn');
        if (checkoutBtn) {
            checkoutBtn.disabled = checkboxes.length === 0;
        }
    },

    // Xử lý chọn tất cả
    handleSelectAll: function(e) {
        const checkboxes = document.querySelectorAll('.item-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = e.target.checked;
        });
        this.updateTotal();
    },

    // Xử lý khi checkbox item thay đổi
    handleItemCheckboxChange: function() {
        this.updateTotal();
    },

    // Áp dụng voucher
    applyVoucher: function() {
        const code = document.getElementById('voucher-code').value;
        const messageElement = document.getElementById('voucher-message');

        if (!code.trim()) {
            messageElement.textContent = 'Vui lòng nhập mã giảm giá';
            messageElement.className = 'mt-1 small text-danger';
            return;
        }

        // Giả lập API kiểm tra voucher
        setTimeout(() => {
            if (code.toUpperCase() === 'VIETTECH10') {
                messageElement.textContent = 'Áp dụng thành tựu giảm giá 10%';
                messageElement.className = 'mt-1 small text-success';

                // Giảm giá 10%
                const subtotalElement = document.getElementById('subtotal-amount');
                const subtotalText = subtotalElement.textContent.replace(/[^\d]/g, '');
                const subtotal = parseInt(subtotalText) || 0;
                const discount = Math.round(subtotal * 0.1);
                const total = subtotal - discount;

                document.getElementById('discount-amount').textContent = `-${this.formatCurrency(discount)}`;
                document.getElementById('total-amount').textContent = this.formatCurrency(total);
            } else {
                messageElement.textContent = 'Mã giảm giá không hợp lệ hoặc đã hết hạn';
                messageElement.className = 'mt-1 small text-danger';
            }
        }, 500);
    },

    // Thanh toán
    checkout: function() {
        const selectedItems = [];
        document.querySelectorAll('.item-checkbox:checked').forEach(checkbox => {
            const productId = checkbox.value;
            const variantId = checkbox.dataset.variantId;
            const quantityInput = document.querySelector(`input[data-product-id="${productId}"][data-variant-id="${variantId}"]`);

            if (quantityInput) {
                selectedItems.push({
                    productId: productId,
                    variantId: variantId,
                    quantity: quantityInput.value
                });
            }
        });

        if (selectedItems.length === 0) {
            alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
            return;
        }

        // Chuyển đến trang thanh toán
        window.location.href = `${window.contextPath}/checkout`;
    },

    // Định dạng tiền tệ
    formatCurrency: function(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0
        }).format(amount);
    },

    // Lấy số lượng sản phẩm trong giỏ hàng
    getCartCount: function() {
        const countElements = document.querySelectorAll('.cart-count-badge');
        if (countElements.length > 0) {
            return parseInt(countElements[0].textContent) || 0;
        }
        return 0;
    },

    // Cập nhật số lượng giỏ hàng trên header
    updateCartCount: function(count) {
        const cartBadges = document.querySelectorAll('.cart-count-badge');
        cartBadges.forEach(badge => {
            if (count > 0) {
                badge.textContent = count;
                badge.style.display = 'block';
            } else {
                badge.style.display = 'none';
            }
        });
    }
};

// Khởi tạo khi DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Lấy context path từ JSP
    window.contextPath = '${pageContext.request.contextPath}';

    // Khởi tạo cart controller (chỉ khi đã đăng nhập và có giỏ hàng)
    if (document.querySelector('.cart-item-row')) {
        cartController.init();
    }
});
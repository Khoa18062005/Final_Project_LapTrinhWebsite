/**
 * Cart AJAX Handler
 * Xử lý thêm sản phẩm vào giỏ hàng từ các trang khác
 */

let cartAjax = {
    // Thêm sản phẩm vào giỏ hàng
    addToCart: function(productId, variantId = 0, quantity = 1) {
        const url = `${window.contextPath}/cart`;
        const formData = new FormData();

        formData.append('action', 'add');
        formData.append('productId', productId);
        formData.append('quantity', quantity);
        if (variantId) {
            formData.append('variantId', variantId);
        }

        return fetch(url, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                // Kiểm tra nếu bị redirect (401 - chưa đăng nhập)
                if (response.redirected) {
                    window.location.href = response.url;
                    return { success: false, redirected: true };
                }

                // Kiểm tra response type
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    return response.json();
                } else {
                    // Nếu response là HTML (có thể do redirect hoặc lỗi server)
                    if (response.ok) {
                        return { success: true, count: null };
                    } else {
                        throw new Error('Server error');
                    }
                }
            })
            .then(data => {
                if (data.redirected) {
                    return data;
                }

                if (data.success) {
                    // Cập nhật badge giỏ hàng
                    if (data.count !== null && data.count !== undefined) {
                        this.updateCartBadge(data.count);
                    } else {
                        // Nếu không có count từ server, tự tăng
                        this.incrementCartBadge();
                    }

                    // Hiển thị thông báo
                    this.showToast('Đã thêm sản phẩm vào giỏ hàng!', 'success');

                    return { success: true, count: data.count };
                } else {
                    this.showToast(data.error || 'Có lỗi xảy ra', 'danger');
                    return { success: false, error: data.error };
                }
            })
            .catch(error => {
                console.error('Error:', error);
                this.showToast('Có lỗi xảy ra khi thêm vào giỏ hàng', 'danger');
                return { success: false, error: error.message };
            });
    },

    // Cập nhật badge với số lượng cụ thể
    updateCartBadge: function(count) {
        const badges = document.querySelectorAll('.cart-count-badge, .cart-count');
        badges.forEach(badge => {
            badge.textContent = count;
            badge.style.display = count > 0 ? 'inline-block' : 'none';

            // Thêm hiệu ứng animation
            if (count > 0) {
                badge.classList.add('badge-animate');
                setTimeout(() => {
                    badge.classList.remove('badge-animate');
                }, 300);
            }
        });
    },

    // Tăng badge lên 1 (dùng khi không có count từ server)
    incrementCartBadge: function() {
        const badge = document.querySelector('.cart-count-badge, .cart-count');
        if (badge) {
            const currentCount = parseInt(badge.textContent) || 0;
            this.updateCartBadge(currentCount + 1);
        }
    },

    // Hiển thị Bootstrap Toast notification
    showToast: function(message, type = 'success') {
        const toastEl = document.getElementById('cartToast');

        // Nếu chưa có toast element, tạo mới
        if (!toastEl) {
            this.createToastElement();
            return this.showToast(message, type); // Gọi lại sau khi tạo
        }

        const toastMessage = document.getElementById('toast-message');
        const toastHeader = toastEl.querySelector('.toast-header');
        const iconElement = toastHeader.querySelector('i');

        // Cập nhật icon
        if (iconElement) {
            iconElement.className = this.getToastIcon(type);
        }

        // Cập nhật nội dung
        toastMessage.textContent = message;

        // Cập nhật màu sắc
        toastHeader.className = `toast-header bg-${type} text-white`;

        // Hiển thị toast
        const bsToast = new bootstrap.Toast(toastEl, {
            animation: true,
            autohide: true,
            delay: 3000
        });
        bsToast.show();
    },

    // Tạo Toast Element nếu chưa có trong HTML
    createToastElement: function() {
        const toastHTML = `
            <div class="position-fixed top-0 end-0 p-3" style="z-index: 9999">
                <div id="cartToast" class="toast" role="alert">
                    <div class="toast-header bg-success text-white">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <strong class="me-auto">Thông báo</strong>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast"></button>
                    </div>
                    <div class="toast-body">
                        <span id="toast-message"></span>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', toastHTML);
    },

    // Lấy icon theo type
    getToastIcon: function(type) {
        const icons = {
            'success': 'bi bi-check-circle-fill me-2',
            'danger': 'bi bi-exclamation-circle-fill me-2',
            'warning': 'bi bi-exclamation-triangle-fill me-2',
            'info': 'bi bi-info-circle-fill me-2'
        };
        return icons[type] || icons['success'];
    },

    // Lấy số lượng giỏ hàng từ server
    fetchCartCount: function() {
        const contextPath = window.contextPath || '';

        return fetch(`${contextPath}/cart/count`)
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch');
                return response.json();
            })
            .then(data => {
                if (data.count !== undefined) {
                    this.updateCartBadge(data.count);
                    return data.count;
                }
            })
            .catch(error => {
                console.error('Error fetching cart count:', error);
            });
    }
};

// ============================================
// KHỞI TẠO KHI DOM READY
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    // Lấy context path từ meta tag hoặc thuộc tính khác
    const contextMeta = document.querySelector('meta[name="context-path"]');
    window.contextPath = contextMeta ? contextMeta.content : '';

    // Xử lý form "Thêm vào giỏ hàng" trên trang chi tiết sản phẩm
    const addToCartForm = document.getElementById('add-to-cart-btn');
    if (addToCartForm) {
        addToCartForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const productId = this.querySelector('input[name="productId"]').value;
            const quantityInput = this.querySelector('input[name="quantity"]');
            const quantity = quantityInput ? parseInt(quantityInput.value) : 1;

            // Lấy variantId từ hidden input
            const variantInput = this.querySelector('input[name="variantId"]');
            let variantId = variantInput ? variantInput.value : '0';

            // Kiểm tra nếu có variant data và chưa chọn variant
            if (window.variantData && window.variantData.variants.length > 0 && (!variantId || variantId === '0')) {
                cartAjax.showToast('Vui lòng chọn phiên bản sản phẩm!', 'warning');
                return;
            }

            // Gọi hàm thêm vào giỏ hàng
            cartAjax.addToCart(productId, variantId, quantity);
        });
    }

    // Xử lý các nút "Thêm vào giỏ" trên trang danh sách (nếu có)
    document.querySelectorAll('.btn-add-to-cart').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = this.dataset.productId;
            const variantId = this.dataset.variantId || '0';

            cartAjax.addToCart(productId, variantId, 1);
        });
    });
});

// Export để sử dụng ở nơi khác nếu cần
if (typeof window !== 'undefined') {
    window.cartAjax = cartAjax;
}
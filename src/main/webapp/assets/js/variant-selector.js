/**
 * Variant Selector JavaScript
 * Quản lý việc chọn và hiển thị variant sản phẩm
 */

let selectedVariant = null;
let currentVariants = [];

// Khởi tạo khi DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Kiểm tra xem có dữ liệu variant không
    if (window.variantData && window.variantData.variants.length > 0) {
        currentVariants = window.variantData.variants;

        // Chọn variant đầu tiên
        selectVariant(currentVariants[0].id);

        // Cập nhật form với variant đầu tiên
        updateAddToCartForm(currentVariants[0].id);
    }
});

/**
 * Chọn một variant theo ID
 */
function selectVariant(variantId) {
    // Tìm variant được chọn
    selectedVariant = currentVariants.find(v => v.id == variantId);

    if (!selectedVariant) {
        console.error('Variant không tồn tại:', variantId);
        return;
    }

    // Cập nhật giá
    updatePrice(selectedVariant.price);

    // Cập nhật thông tin variant
    updateVariantInfo(selectedVariant);

    // Cập nhật thông tin variant trong specifications
    updateVariantSpecs(selectedVariant.attributes);

    // Cập nhật class active cho các button
    updateActiveButtons(variantId);

    // Cập nhật form
    updateAddToCartForm(variantId);
}

/**
 * Cập nhật giá sản phẩm
 */
function updatePrice(price) {
    const priceElement = document.getElementById('dynamic-price');
    if (priceElement) {
        // Format giá thành tiền Việt
        const formattedPrice = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);

        // Thêm animation
        priceElement.classList.add('price-update');
        priceElement.textContent = formattedPrice;

        // Xóa class animation sau khi hoàn thành
        setTimeout(() => {
            priceElement.classList.remove('price-update');
        }, 500);
    }
}

/**
 * Cập nhật thông tin variant trong phần variant info
 */
function updateVariantInfo(variant) {
    const variantInfoElement = document.getElementById('variant-info');
    if (!variantInfoElement) return;

    let html = '';

    // Hiển thị các attribute của variant
    if (variant.attributes && variant.attributes.length > 0) {
        variant.attributes.forEach(attr => {
            html += `<div class="variant-attribute">
                        <strong>${attr.name}:</strong>
                        <span>${attr.value}</span>
                     </div>`;
        });
    }

    variantInfoElement.innerHTML = html;
}

/**
 * Cập nhật thông tin variant trong phần specifications
 */
function updateVariantSpecs(attributes) {
    const variantSpecsContainer = document.getElementById('variant-specs-container');
    if (!variantSpecsContainer) return;

    // Xóa nội dung cũ
    variantSpecsContainer.innerHTML = '';

    if (!attributes || attributes.length === 0) {
        variantSpecsContainer.style.display = 'none';
        return;
    }

    // Hiển thị container
    variantSpecsContainer.style.display = 'block';

    let html = '<div class="specs-grid">';

    attributes.forEach(attr => {
        html += `<div class="spec-item">
                    <span class="spec-label">${attr.name}:</span>
                    <span class="spec-value">${attr.value}</span>
                 </div>`;
    });

    html += '</div>';
    variantSpecsContainer.innerHTML = html;
}

/**
 * Cập nhật class active cho các button variant
 */
function updateActiveButtons(selectedVariantId) {
    // Xóa class active từ tất cả các button
    document.querySelectorAll('.variant-option').forEach(btn => {
        btn.classList.remove('active');
        btn.classList.remove('btn-primary');
        btn.classList.add('btn-outline-primary');
    });

    // Thêm class active cho button được chọn
    const selectedBtn = document.querySelector(`.variant-option[data-variant-id="${selectedVariantId}"]`);
    if (selectedBtn) {
        selectedBtn.classList.add('active');
        selectedBtn.classList.remove('btn-outline-primary');
        selectedBtn.classList.add('btn-primary');
    }
}

/**
 * Cập nhật form thêm vào giỏ hàng
 */
function updateAddToCartForm(variantId) {
    const addToCartForm = document.getElementById('add-to-cart-form');
    const buyNowForm = document.getElementById('buy-now-form');

    // Cập nhật form thêm vào giỏ hàng
    if (addToCartForm) {
        const variantInput = document.getElementById('selected-variant-id');
        if (variantInput) {
            variantInput.value = variantId;
        } else {
            // Tạo input nếu chưa có
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'variantId';
            input.id = 'selected-variant-id';
            input.value = variantId;
            addToCartForm.appendChild(input);
        }

        // Disable/enable nút thêm vào giỏ
        const addToCartBtn = document.getElementById('add-to-cart-btn');
        const variant = currentVariants.find(v => v.id == variantId);

        if (addToCartBtn && variant) {
            addToCartBtn.disabled = !variant.isActive;
            if (!variant.isActive) {
                addToCartBtn.title = 'Phiên bản này đã hết hàng';
                addToCartBtn.innerHTML = '<i class="bi bi-cart-x"></i> Hết hàng';
                addToCartBtn.classList.remove('btn-primary');
                addToCartBtn.classList.add('btn-secondary');
            } else {
                addToCartBtn.title = '';
                addToCartBtn.innerHTML = '<i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng';
                addToCartBtn.classList.remove('btn-secondary');
                addToCartBtn.classList.add('btn-primary');
            }
        }
    }

    // Cập nhật form mua ngay
    if (buyNowForm) {
        const variantInput = document.getElementById('buy-now-variant-id');
        const buyNowBtn = document.getElementById('buy-now-btn');
        const variant = currentVariants.find(v => v.id == variantId);

        if (variantInput) {
            variantInput.value = variantId;
        } else {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'variantId';
            input.id = 'buy-now-variant-id';
            input.value = variantId;
            buyNowForm.appendChild(input);
        }

        if (buyNowBtn && variant) {
            buyNowBtn.disabled = !variant.isActive;
            if (!variant.isActive) {
                buyNowBtn.title = 'Phiên bản này đã hết hàng';
            } else {
                buyNowBtn.title = '';
            }
        }
    }
}

/**
 * Lấy variant hiện tại được chọn
 */
function getSelectedVariant() {
    return selectedVariant;
}

/**
 * Lấy tất cả variants
 */
function getAllVariants() {
    return currentVariants;
}

// Xuất các hàm ra global scope
window.selectVariant = selectVariant;
window.getSelectedVariant = getSelectedVariant;
window.getAllVariants = getAllVariants;
/**
 * Voucher Handler for Shipping Info Page
 * Handles voucher selection, application, and price calculation
 */

document.addEventListener('DOMContentLoaded', function() {
    // Elements
    const voucherSelect = document.getElementById('voucherCode');
    const applyVoucherBtn = document.getElementById('applyVoucherBtn');
    const removeVoucherBtn = document.getElementById('removeVoucherBtn');
    const selectedVoucherInfo = document.getElementById('selectedVoucherInfo');
    const voucherAppliedText = document.getElementById('voucherAppliedText');
    const voucherDescription = document.getElementById('voucherDescription');
    const voucherDiscountInput = document.getElementById('voucherDiscount');
    const finalTotalElement = document.getElementById('finalTotal');

    // Current order data
    let currentOrder = {
        subtotal: orderData.subtotal || 0,
        shippingFee: orderData.shippingFee || 0,
        discount: 0,
        total: orderData.currentTotal || 0
    };

    // Initialize
    initVoucherHandler();

    function initVoucherHandler() {
        // Apply voucher button click
        if (applyVoucherBtn) {
            applyVoucherBtn.addEventListener('click', applyVoucher);
        }

        // Remove voucher button click
        if (removeVoucherBtn) {
            removeVoucherBtn.addEventListener('click', removeVoucher);
        }

        // Voucher select change
        if (voucherSelect) {
            voucherSelect.addEventListener('change', updateVoucherDescription);
        }

        // Check for pre-selected voucher from session
        checkExistingVoucher();
    }

    function applyVoucher() {
        const selectedValue = voucherSelect.value;

        if (!selectedValue) {
            showNotification('Vui lòng chọn một voucher', 'warning');
            return;
        }

        const selectedOption = voucherSelect.options[voucherSelect.selectedIndex];
        const voucherCode = selectedOption.value;
        const discountType = selectedOption.dataset.discountType;
        const discountValue = parseFloat(selectedOption.dataset.discountValue) || 0;

        // Validate voucher
        if (!validateVoucher(selectedOption)) {
            return;
        }

        // Calculate discount
        let discountAmount = 0;

        switch (discountType) {
            case 'percentage':
                discountAmount = (currentOrder.subtotal * discountValue) / 100;
                // Check max discount if applicable
                const maxDiscount = parseFloat(selectedOption.dataset.maxDiscount) || 0;
                if (maxDiscount > 0 && discountAmount > maxDiscount) {
                    discountAmount = maxDiscount;
                }
                break;

            case 'fixed':
                discountAmount = discountValue;
                break;

            case 'shipping':
                discountAmount = currentOrder.shippingFee;
                break;
        }

        // Ensure discount doesn't exceed total
        if (discountAmount > currentOrder.subtotal) {
            discountAmount = currentOrder.subtotal;
        }

        // Update order totals
        currentOrder.discount = discountAmount;
        currentOrder.total = currentOrder.subtotal + currentOrder.shippingFee - discountAmount;

        // Update UI
        updateOrderDisplay(discountAmount);

        // Show voucher applied message
        showVoucherApplied(voucherCode, discountAmount);

        // Save to hidden field
        voucherDiscountInput.value = discountAmount;

        // Update form action with voucher parameter
        updateFormAction(voucherCode);

        showNotification('Áp dụng voucher thành công!', 'success');
    }

    function removeVoucher() {
        // Reset order totals
        currentOrder.discount = 0;
        currentOrder.total = currentOrder.subtotal + currentOrder.shippingFee;

        // Update UI
        updateOrderDisplay(0);

        // Hide voucher info
        selectedVoucherInfo.style.display = 'none';
        voucherDescription.style.display = 'block';

        // Reset select
        voucherSelect.value = '';

        // Reset hidden field
        voucherDiscountInput.value = 0;

        // Reset form action
        updateFormAction('');

        showNotification('Đã xóa voucher', 'info');
    }

    function updateVoucherDescription() {
        const selectedOption = voucherSelect.options[voucherSelect.selectedIndex];

        if (!selectedOption.value) {
            voucherDescription.innerHTML = '<i class="bi bi-info-circle"></i> Chọn voucher để nhận ưu đãi';
            return;
        }

        const voucherCode = selectedOption.value;
        const discountType = selectedOption.dataset.discountType;
        const discountValue = parseFloat(selectedOption.dataset.discountValue) || 0;

        let description = '';

        switch (discountType) {
            case 'percentage':
                description = `Giảm ${discountValue}% trên tổng đơn hàng`;
                break;
            case 'fixed':
                description = `Giảm ${formatCurrency(discountValue)} thẳng vào đơn hàng`;
                break;
            case 'shipping':
                description = `Miễn phí vận chuyển lên đến ${formatCurrency(discountValue)}`;
                break;
            default:
                description = 'Voucher khuyến mãi';
        }

        voucherDescription.innerHTML = `<i class="bi bi-tag"></i> ${voucherCode}: ${description}`;
    }

    function validateVoucher(voucherOption) {
        const voucherCode = voucherOption.value;
        const minOrderValue = parseFloat(voucherOption.dataset.minOrderValue) || 0;

        // Check minimum order value
        if (currentOrder.subtotal < minOrderValue) {
            showNotification(
                `Voucher ${voucherCode} yêu cầu đơn hàng tối thiểu ${formatCurrency(minOrderValue)}`,
                'warning'
            );
            return false;
        }

        // Additional validation can be added here
        // (check expiry, usage limit, etc.)

        return true;
    }

    function updateOrderDisplay(discountAmount) {
        // Update total display
        if (finalTotalElement) {
            finalTotalElement.textContent = formatCurrency(currentOrder.total);
        }

        // Update discount display in order summary
        updateDiscountInSummary(discountAmount);
    }

    function updateDiscountInSummary(discountAmount) {
        // Find or create discount row
        let discountRow = document.querySelector('.price-row.discount');

        if (discountAmount > 0) {
            if (!discountRow) {
                discountRow = document.createElement('div');
                discountRow.className = 'price-row discount';
                discountRow.innerHTML = `
                    <span>Giảm giá voucher:</span>
                    <span class="price-value">-${formatCurrency(discountAmount)}</span>
                `;

                // Insert before total section
                const priceSummary = document.querySelector('.price-summary');
                if (priceSummary) {
                    priceSummary.appendChild(discountRow);
                }
            } else {
                discountRow.querySelector('.price-value').textContent =
                    '-' + formatCurrency(discountAmount);
            }
        } else if (discountRow) {
            discountRow.remove();
        }
    }

    function showVoucherApplied(voucherCode, discountAmount) {
        let discountText = '';

        if (discountAmount === currentOrder.shippingFee) {
            discountText = 'Miễn phí vận chuyển';
        } else {
            discountText = `Giảm ${formatCurrency(discountAmount)}`;
        }

        voucherAppliedText.innerHTML = `
            <strong>${voucherCode}</strong> đã được áp dụng. ${discountText}
        `;

        selectedVoucherInfo.style.display = 'block';
        voucherDescription.style.display = 'none';
    }

    function checkExistingVoucher() {
        // Check if there's a voucher in URL parameters
        const urlParams = new URLSearchParams(window.location.search);
        const voucherParam = urlParams.get('voucher');

        if (voucherParam && orderData.availableVouchers) {
            const voucher = orderData.availableVouchers.find(v => v.code === voucherParam);
            if (voucher) {
                // Select the voucher in dropdown
                voucherSelect.value = voucherParam;
                updateVoucherDescription();

                // Auto-apply if valid
                setTimeout(() => applyVoucher(), 500);
            }
        }
    }

    function updateFormAction(voucherCode) {
        const form = document.getElementById('addressForm');
        if (form) {
            let action = contextPath + '/checkout';
            if (voucherCode) {
                action += '?voucher=' + encodeURIComponent(voucherCode);
            }
            form.action = action;
        }
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    function showNotification(message, type = 'info') {
        // Use existing notification system or create simple alert
        if (typeof showToast === 'function') {
            showToast(message, type);
        } else {
            alert(message);
        }
    }
});
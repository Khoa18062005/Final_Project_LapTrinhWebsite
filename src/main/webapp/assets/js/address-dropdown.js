/**
 * Address Dropdown Handler
 * Chuyển đổi danh sách địa chỉ thành dropdown và xử lý responsive
 */

(function() {
    'use strict';

    // Hàm khởi tạo dropdown địa chỉ
    function initAddressDropdown() {
        const addressForm = document.getElementById('addressForm');
        const addressList = document.querySelector('.address-list');

        if (!addressList || !addressForm) {
            console.log('Không tìm thấy danh sách địa chỉ hoặc form');
            return;
        }

        // Chỉ tạo dropdown trên desktop (màn hình lớn hơn 768px)
        if (window.innerWidth <= 768) {
            // Trên mobile, hiển thị danh sách card
            addressList.style.display = 'block';
            return;
        }

        // Tìm địa chỉ mặc định
        let defaultAddress = null;
        const defaultRadio = document.querySelector('input[name="selectedAddressId"]:checked');
        if (defaultRadio) {
            const defaultCard = defaultRadio.closest('.card');
            defaultAddress = {
                id: defaultRadio.value,
                element: defaultCard
            };
        }

        // Tạo dropdown container
        const dropdownContainer = document.createElement('div');
        dropdownContainer.className = 'address-dropdown mb-4';

        // Tạo select element
        const selectElement = document.createElement('select');
        selectElement.className = 'form-select form-select-lg';
        selectElement.name = 'selectedAddressId';
        selectElement.id = 'addressSelect';
        selectElement.required = true;

        // Thêm option mặc định
        const defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.textContent = '-- Chọn địa chỉ giao hàng --';
        defaultOption.disabled = true;
        defaultOption.selected = !defaultAddress; // Chỉ selected nếu không có địa chỉ mặc định
        selectElement.appendChild(defaultOption);

        // Thêm địa chỉ mặc định đầu tiên nếu có
        if (defaultAddress) {
            const defaultOptionText = createAddressOptionText(defaultAddress.element);
            const option = createOptionElement(defaultAddress.id, defaultOptionText, true, defaultAddress.element);
            selectElement.appendChild(option);
            selectElement.value = defaultAddress.id; // Chọn địa chỉ mặc định
        }

        // Lấy tất cả địa chỉ từ danh sách radio (trừ địa chỉ mặc định đã thêm)
        const addressRadios = Array.from(document.querySelectorAll('input[name="selectedAddressId"]'));

        addressRadios.forEach((radio) => {
            // Bỏ qua địa chỉ mặc định đã thêm
            if (defaultAddress && radio.value === defaultAddress.id) {
                return;
            }

            const addressCard = radio.closest('.card');
            const optionText = createAddressOptionText(addressCard);
            const isChecked = radio.checked;
            const option = createOptionElement(radio.value, optionText, isChecked, addressCard);
            selectElement.appendChild(option);
        });

        // Tạo container hiển thị địa chỉ đã chọn
        const previewContainer = document.createElement('div');
        previewContainer.className = 'selected-address-preview';
        previewContainer.id = 'addressPreview';

        // Thêm vào DOM
        dropdownContainer.appendChild(selectElement);
        addressList.parentNode.insertBefore(dropdownContainer, addressList);
        dropdownContainer.parentNode.insertBefore(previewContainer, dropdownContainer.nextSibling);

        // Ẩn danh sách địa chỉ cũ
        addressList.style.display = 'none';

        // Hiển thị preview cho địa chỉ mặc định ngay lập tức
        if (defaultAddress) {
            updateAddressPreview(defaultAddress.element);
        }

        // Xử lý sự kiện
        setupEventListeners(selectElement, previewContainer);

        // Xử lý responsive khi thay đổi kích thước màn hình
        setupResponsiveBehavior(addressList, dropdownContainer, previewContainer);
    }

    // Hàm tạo text cho option
    function createAddressOptionText(addressCard) {
        const nameElement = addressCard.querySelector('strong');
        const phoneMatch = addressCard.innerHTML.match(/\| ([0-9+\s]+)</);
        const addressLines = addressCard.querySelectorAll('p');

        let optionText = '';

        if (nameElement) {
            optionText += nameElement.textContent.trim();
        }

        if (phoneMatch) {
            optionText += ' | ' + phoneMatch[1];
        }

        // Kiểm tra có phải địa chỉ mặc định không
        const isDefault = addressCard.querySelector('.badge.bg-primary');
        if (isDefault) {
            optionText += ' ★ Mặc định';
        }

        return optionText;
    }

    // Hàm tạo option element
    function createOptionElement(value, text, isSelected, addressCard) {
        const option = document.createElement('option');
        option.value = value;
        option.textContent = text;
        option.selected = isSelected;

        // Lưu thông tin đầy đủ vào dataset để có thể sử dụng sau
        if (addressCard) {
            option.dataset.cardId = value;
        }

        return option;
    }

    // Hàm cập nhật preview địa chỉ
    function updateAddressPreview(addressCard) {
        const previewContainer = document.getElementById('addressPreview');
        if (!previewContainer || !addressCard) return;

        const name = addressCard.querySelector('strong')?.textContent || '';
        const phoneMatch = addressCard.innerHTML.match(/\| ([0-9+\s]+)</);
        const phone = phoneMatch ? phoneMatch[1] : '';
        const addressLines = addressCard.querySelectorAll('p');
        const isDefault = addressCard.querySelector('.badge.bg-primary');

        previewContainer.innerHTML = `
            <div class="d-flex justify-content-between align-items-start">
                <div>
                    <strong class="d-block mb-1">${name}</strong>
                    <span class="text-muted d-block mb-2">${phone}</span>
                    ${Array.from(addressLines).map(p => `<p class="mb-1">${p.textContent}</p>`).join('')}
                </div>
                ${isDefault ? '<span class="badge bg-primary">Mặc định</span>' : ''}
            </div>
        `;
        previewContainer.style.display = 'block';
    }

    // Hàm thiết lập event listeners
    function setupEventListeners(selectElement, previewContainer) {
        // Xử lý khi chọn địa chỉ từ dropdown
        selectElement.addEventListener('change', function() {
            const addressId = this.value;

            if (!addressId) {
                previewContainer.style.display = 'none';
                return;
            }

            // Cập nhật radio button ẩn
            const radio = document.querySelector(`input[value="${addressId}"]`);
            if (radio) {
                radio.checked = true;
                const addressCard = radio.closest('.card');
                updateAddressPreview(addressCard);
            }
        });

        // Xử lý submit form
        const addressForm = document.getElementById('addressForm');
        addressForm.addEventListener('submit', function(e) {
            if (!selectElement.value) {
                e.preventDefault();
                showAlert('Vui lòng chọn một địa chỉ giao hàng', 'warning');
                selectElement.focus();
                return false;
            }
            return true;
        });
    }

    // Hàm xử lý responsive behavior
    function setupResponsiveBehavior(addressList, dropdownContainer, previewContainer) {
        function checkScreenSize() {
            const isMobile = window.innerWidth <= 768;

            if (isMobile) {
                // Mobile: hiển thị danh sách, ẩn dropdown và preview
                if (dropdownContainer) dropdownContainer.style.display = 'none';
                if (previewContainer) previewContainer.style.display = 'none';
                addressList.style.display = 'block';
            } else {
                // Desktop: hiển thị dropdown và preview, ẩn danh sách
                if (dropdownContainer) dropdownContainer.style.display = 'block';
                addressList.style.display = 'none';
            }
        }

        // Kiểm tra khi thay đổi kích thước màn hình
        window.addEventListener('resize', checkScreenSize);
    }

    // Hàm hiển thị thông báo
    function showAlert(message, type = 'info') {
        // Kiểm tra xem đã có alert container chưa
        let alertContainer = document.getElementById('addressAlertContainer');

        if (!alertContainer) {
            alertContainer = document.createElement('div');
            alertContainer.id = 'addressAlertContainer';
            alertContainer.className = 'position-fixed top-0 end-0 p-3';
            alertContainer.style.zIndex = '1060';
            document.body.appendChild(alertContainer);
        }

        const alertId = 'alert-' + Date.now();
        const alertHtml = `
            <div id="${alertId}" class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;

        alertContainer.insertAdjacentHTML('afterbegin', alertHtml);

        // Tự động ẩn sau 5 giây
        setTimeout(() => {
            const alertElement = document.getElementById(alertId);
            if (alertElement) {
                const bsAlert = new bootstrap.Alert(alertElement);
                bsAlert.close();
            }
        }, 5000);
    }

    // Hàm khởi tạo tất cả
    function init() {
        // Đợi DOM sẵn sàng
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', initAddressDropdown);
        } else {
            initAddressDropdown();
        }
    }

    // Khởi chạy
    init();

    // Export cho sử dụng bên ngoài nếu cần
    window.AddressDropdown = {
        init: initAddressDropdown,
        refresh: initAddressDropdown
    };

})();
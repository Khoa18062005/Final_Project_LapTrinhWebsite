document.addEventListener('DOMContentLoaded', function() {
    console.log('✅ Address.js loaded');

    initAddressForm();
    initSetDefaultButtons();
});

// ========== KHỞI TẠO FORM THÊM ĐỊA CHỈ ==========
function initAddressForm() {
    const apiBaseUrl = 'https://provinces.open-api.vn/api';

    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');
    const phoneInput = document.getElementById('phone'); // ← MỚI THÊM

    if (!citySelect || !districtSelect || !wardSelect) {
        console.warn('⚠️ Address form elements not found');
        return;
    }

    // Load cities khi trang load
    loadCities();

    // ===== VALIDATION SỐ ĐIỆN THOẠI ===== (MỚI THÊM - BẮT ĐẦU)
    if (phoneInput) {
        // Chỉ cho phép nhập số
        phoneInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
            if (this.value.length > 10) {
                this.value = this.value.slice(0, 10);
            }
            validatePhone(this);
        });

        phoneInput.addEventListener('blur', function() {
            validatePhone(this);
        });

        phoneInput.addEventListener('focus', function() {
            this.classList.remove('is-invalid', 'is-valid');
            const feedback = this.parentElement.querySelector('.invalid-feedback');
            if (feedback) feedback.remove();
        });
    }
    // ===== VALIDATION SỐ ĐIỆN THOẠI ===== (MỚI THÊM - KẾT THÚC)

    // Event listeners
    citySelect.addEventListener('change', function() {
        const cityCode = this.value;
        if (cityCode) {
            loadDistricts(cityCode);
        } else {
            resetSelect(districtSelect, 'Chọn quận/huyện');
            resetSelect(wardSelect, 'Chọn phường/xã');
        }
    });

    districtSelect.addEventListener('change', function() {
        const districtCode = this.value;
        if (districtCode) {
            loadWards(districtCode);
        } else {
            resetSelect(wardSelect, 'Chọn phường/xã');
        }
    });

    // ===== LOAD CITIES =====
    function loadCities() {
        console.log('📍 Loading cities...');
        citySelect.disabled = true;

        fetch(`${apiBaseUrl}/p/`)
            .then(response => response.json())
            .then(data => {
                console.log(`✅ Loaded ${data.length} cities`);

                citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';

                data.forEach(city => {
                    const option = document.createElement('option');
                    option.value = city.code;
                    option.textContent = city.name;
                    option.setAttribute('data-name', city.name);
                    citySelect.appendChild(option);
                });

                citySelect.disabled = false;
            })
            .catch(error => {
                console.error('❌ Error loading cities:', error);
                citySelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
            });
    }

    // ===== LOAD DISTRICTS =====
    function loadDistricts(cityCode) {
        console.log(`📍 Loading districts for city: ${cityCode}`);
        districtSelect.disabled = true;
        resetSelect(wardSelect, 'Chọn phường/xã');

        fetch(`${apiBaseUrl}/p/${cityCode}?depth=2`)
            .then(response => response.json())
            .then(data => {
                console.log(`✅ Loaded ${data.districts.length} districts`);

                districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';

                data.districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.code;
                    option.textContent = district.name;
                    option.setAttribute('data-name', district.name);
                    districtSelect.appendChild(option);
                });

                districtSelect.disabled = false;
            })
            .catch(error => {
                console.error('❌ Error loading districts:', error);
                districtSelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
            });
    }

    // ===== LOAD WARDS =====
    function loadWards(districtCode) {
        console.log(`📍 Loading wards for district: ${districtCode}`);
        wardSelect.disabled = true;

        fetch(`${apiBaseUrl}/d/${districtCode}?depth=2`)
            .then(response => response.json())
            .then(data => {
                console.log(`✅ Loaded ${data.wards.length} wards`);

                wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';

                data.wards.forEach(ward => {
                    const option = document.createElement('option');
                    option.value = ward.code;
                    option.textContent = ward.name;
                    option.setAttribute('data-name', ward.name);
                    wardSelect.appendChild(option);
                });

                wardSelect.disabled = false;
            })
            .catch(error => {
                console.error('❌ Error loading wards:', error);
                wardSelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
            });
    }

    // ===== RESET SELECT =====
    function resetSelect(selectElement, placeholder) {
        selectElement.innerHTML = `<option value="" selected disabled>${placeholder}</option>`;
        selectElement.disabled = true;
    }

    // ===== FORM SUBMIT - ĐỔI CODE THÀNH TÊN =====
    const addAddressForm = document.getElementById('addAddressForm');
    if (addAddressForm) {
        addAddressForm.addEventListener('submit', function(e) {
            console.log('📤 Form submitting...');

            // ===== VALIDATE PHONE TRƯỚC KHI SUBMIT ===== (MỚI THÊM - BẮT ĐẦU)
            if (phoneInput && !validatePhone(phoneInput)) {
                e.preventDefault();
                console.error('❌ Phone validation failed');
                phoneInput.focus();
                return false;
            }
            // ===== VALIDATE PHONE TRƯỚC KHI SUBMIT ===== (MỚI THÊM - KẾT THÚC)

            // Lấy selected options
            const cityOption = citySelect.options[citySelect.selectedIndex];
            const districtOption = districtSelect.options[districtSelect.selectedIndex];
            const wardOption = wardSelect.options[wardSelect.selectedIndex];

            // Lấy tên từ data-name hoặc textContent
            const cityName = cityOption ? (cityOption.getAttribute('data-name') || cityOption.textContent) : '';
            const districtName = districtOption ? (districtOption.getAttribute('data-name') || districtOption.textContent) : '';
            const wardName = wardOption ? (wardOption.getAttribute('data-name') || wardOption.textContent) : '';

            console.log('✅ Extracted names:', {
                city: cityName,
                district: districtName,
                ward: wardName
            });

            // Tạo hidden inputs để gửi TÊN thay vì CODE
            if (cityName) {
                const hiddenCity = document.createElement('input');
                hiddenCity.type = 'hidden';
                hiddenCity.name = 'city';
                hiddenCity.value = cityName;
                addAddressForm.appendChild(hiddenCity);

                // Disable select gốc để không gửi code
                citySelect.disabled = true;
            }

            if (districtName) {
                const hiddenDistrict = document.createElement('input');
                hiddenDistrict.type = 'hidden';
                hiddenDistrict.name = 'district';
                hiddenDistrict.value = districtName;
                addAddressForm.appendChild(hiddenDistrict);

                districtSelect.disabled = true;
            }

            if (wardName) {
                const hiddenWard = document.createElement('input');
                hiddenWard.type = 'hidden';
                hiddenWard.name = 'ward';
                hiddenWard.value = wardName;
                addAddressForm.appendChild(hiddenWard);

                wardSelect.disabled = true;
            }
            console.log('✅ Hidden inputs created, form ready to submit');
        });
    }
}

// ===== HÀM VALIDATE SỐ ĐIỆN THOẠI ===== (MỚI THÊM - TOÀN BỘ HÀM)
function validatePhone(phoneInput) {
    const phoneValue = phoneInput.value.trim();

    const oldFeedback = phoneInput.parentElement.querySelector('.invalid-feedback');
    if (oldFeedback) oldFeedback.remove();

    if (phoneValue === '') {
        phoneInput.classList.remove('is-valid', 'is-invalid');
        return false;
    }

    const isValid = /^0\d{9}$/.test(phoneValue);

    if (isValid) {
        phoneInput.classList.remove('is-invalid');
        phoneInput.classList.add('is-valid');
        return true;
    } else {
        phoneInput.classList.remove('is-valid');
        phoneInput.classList.add('is-invalid');

        const feedback = document.createElement('div');
        feedback.className = 'invalid-feedback d-block';

        if (!phoneValue.startsWith('0')) {
            feedback.textContent = 'Số điện thoại phải bắt đầu bằng số 0';
        } else if (phoneValue.length < 10) {
            feedback.textContent = `Số điện thoại phải đủ 10 số (còn thiếu ${10 - phoneValue.length} số)`;
        } else {
            feedback.textContent = 'Số điện thoại không hợp lệ';
        }

        phoneInput.parentElement.appendChild(feedback);
        return false;
    }
}

// ========== KHỞI TẠO NÚT ĐẶT MẶC ĐỊNH ==========
function initSetDefaultButtons() {
    const setDefaultButtons = document.querySelectorAll('.btn-set-default');

    console.log(`🔘 Found ${setDefaultButtons.length} set-default buttons`);

    setDefaultButtons.forEach(button => {
        button.addEventListener('click', function() {
            const addressId = this.getAttribute('data-address-id');
            const customerId = this.getAttribute('data-customer-id');

            console.log(`🎯 Set default clicked for address: ${addressId}`);

            setDefaultAddress(addressId, customerId, this);
        });
    });
}

// ========== AJAX SET DEFAULT ADDRESS ==========
function setDefaultAddress(addressId, customerId, buttonElement) {
    // Disable button để tránh spam click
    buttonElement.disabled = true;
    const originalHtml = buttonElement.innerHTML;
    buttonElement.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang xử lý...';

    const contextPath = document.querySelector('meta[name="context-path"]').content;

    // Gửi AJAX request
    fetch(contextPath + '/profile/address/set-default', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `addressId=${addressId}&customerId=${customerId}`
    })
        .then(response => response.json())
        .then(data => {
            console.log('📡 Server response:', data);

            if (data.success) {
                console.log('✅ Set default address successfully');

                // Hiển thị thông báo thành công
                showToast('success', 'Đã đặt làm địa chỉ mặc định!');

                // Reload trang sau 500ms để cập nhật UI
                setTimeout(() => {
                    window.location.reload();
                }, 500);

            } else {
                console.error('❌ Set default failed:', data.message);
                showToast('error', data.message || 'Có lỗi xảy ra!');

                // Khôi phục button
                buttonElement.disabled = false;
                buttonElement.innerHTML = originalHtml;
            }
        })
        .catch(error => {
            console.error('❌ AJAX error:', error);
            showToast('error', 'Lỗi kết nối! Vui lòng thử lại.');

            // Khôi phục button
            buttonElement.disabled = false;
            buttonElement.innerHTML = originalHtml;
        });
}

// ========== HIỂN THỊ TOAST NOTIFICATION ==========
function showToast(type, message) {
    // Tạo toast element
    const toastContainer = document.getElementById('toastContainer') || createToastContainer();

    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type === 'success' ? 'success' : 'danger'} border-0`;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');

    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-${type === 'success' ? 'check-circle' : 'x-circle'} me-2"></i>
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;

    toastContainer.appendChild(toast);

    // Show toast
    const bsToast = new bootstrap.Toast(toast, {
        autohide: true,
        delay: 3000
    });
    bsToast.show();

    // Remove sau khi ẩn
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}

// ========== TẠO TOAST CONTAINER ==========
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}
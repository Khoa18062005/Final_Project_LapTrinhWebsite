// Biến lưu dữ liệu API
let provinces = [];
let districts = [];
let wards = [];

// Khi modal hiển thị
document.getElementById('addAddressModal').addEventListener('shown.bs.modal', function () {
    loadProvinces();
});

// Load danh sách tỉnh/thành phố từ API
function loadProvinces() {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    // Reset các select
    citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';
    districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
    districtSelect.disabled = true;
    wardSelect.disabled = true;

    // Sử dụng API của Tỉnh thành Việt Nam
    fetch('https://provinces.open-api.vn/api/')
        .then(response => response.json())
        .then(data => {
            provinces = data;
            data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.code;
                option.textContent = province.name;
                citySelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách tỉnh/thành:', error);
            loadStaticProvinces();
        });
}

// Fallback: Danh sách tỉnh/thành tĩnh
function loadStaticProvinces() {
    const staticProvinces = [
        { code: '01', name: 'Hà Nội' },
        { code: '79', name: 'TP. Hồ Chí Minh' },
        { code: '31', name: 'Hải Phòng' },
        { code: '48', name: 'Đà Nẵng' },
        { code: '92', name: 'Cần Thơ' }
    ];

    const citySelect = document.getElementById('city');
    staticProvinces.forEach(province => {
        const option = document.createElement('option');
        option.value = province.code;
        option.textContent = province.name;
        citySelect.appendChild(option);
    });
}

// Khi chọn tỉnh/thành phố
document.getElementById('city').addEventListener('change', function() {
    const provinceCode = this.value;
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');
    const cityNameInput = document.getElementById('cityName');

    // Lưu tên tỉnh vào hidden input
    const selectedOption = this.options[this.selectedIndex];
    cityNameInput.value = selectedOption.textContent;

    if (!provinceCode) {
        districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
        wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
        districtSelect.disabled = true;
        wardSelect.disabled = true;
        return;
    }

    // Reset các select cấp dưới
    districtSelect.innerHTML = '<option value="" selected disabled>Đang tải...</option>';
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
    districtSelect.disabled = true;
    wardSelect.disabled = true;

    // Gọi API lấy quận/huyện của tỉnh đã chọn
    fetch(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`)
        .then(response => response.json())
        .then(province => {
            districts = province.districts || [];
            districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';

            if (districts.length > 0) {
                districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.code;
                    option.textContent = district.name;
                    districtSelect.appendChild(option);
                });
                districtSelect.disabled = false;
            } else {
                districtSelect.innerHTML = '<option value="" selected disabled>Không có quận/huyện</option>';
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách quận/huyện:', error);
            districtSelect.innerHTML = '<option value="" selected disabled>Lỗi khi tải dữ liệu</option>';
        });
});

// Khi chọn quận/huyện
document.getElementById('district').addEventListener('change', function() {
    const districtCode = this.value;
    const wardSelect = document.getElementById('ward');
    const districtNameInput = document.getElementById('districtName');

    // Lưu tên quận vào hidden input
    const selectedOption = this.options[this.selectedIndex];
    districtNameInput.value = selectedOption.textContent;

    if (!districtCode) {
        wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
        wardSelect.disabled = true;
        return;
    }

    // Reset select phường/xã
    wardSelect.innerHTML = '<option value="" selected disabled>Đang tải...</option>';
    wardSelect.disabled = true;

    // Gọi API lấy phường/xã của quận/huyện đã chọn
    fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
        .then(response => response.json())
        .then(district => {
            wards = district.wards || [];
            wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';

            if (wards.length > 0) {
                wards.forEach(ward => {
                    const option = document.createElement('option');
                    option.value = ward.code;
                    option.textContent = ward.name;
                    wardSelect.appendChild(option);
                });
                wardSelect.disabled = false;
            } else {
                wardSelect.innerHTML = '<option value="" selected disabled>Không có phường/xã</option>';
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách phường/xã:', error);
            wardSelect.innerHTML = '<option value="" selected disabled>Lỗi khi tải dữ liệu</option>';
        });
});

// Khi chọn phường/xã
document.getElementById('ward').addEventListener('change', function() {
    const wardNameInput = document.getElementById('wardName');
    const selectedOption = this.options[this.selectedIndex];
    wardNameInput.value = selectedOption.textContent;
});

// Xử lý submit form
function submitAddressForm() {
    const form = document.getElementById('addAddressForm');
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    // Kiểm tra xem đã chọn đủ chưa
    if (!citySelect.value || !districtSelect.value || !wardSelect.value) {
        alert('Vui lòng chọn đầy đủ tỉnh/thành phố, quận/huyện và phường/xã!');
        return false;
    }

    // Validate form
    if (!form.checkValidity()) {
        form.reportValidity();
        return false;
    }

    // Hiển thị loading trên nút submit
    const submitBtn = document.querySelector('#addAddressForm button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...';
    submitBtn.disabled = true;

    return true; // Cho phép form submit
}

// Reset form khi modal đóng
document.getElementById('addAddressModal').addEventListener('hidden.bs.modal', function () {
    const form = document.getElementById('addAddressForm');
    form.reset();

    // Reset các select
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
    districtSelect.disabled = true;
    wardSelect.disabled = true;

    // Reset hidden inputs
    document.getElementById('cityName').value = '';
    document.getElementById('districtName').value = '';
    document.getElementById('wardName').value = '';

    // Reset nút submit
    const submitBtn = document.querySelector('#addAddressForm button[type="submit"]');
    submitBtn.innerHTML = 'Lưu địa chỉ';
    submitBtn.disabled = false;
});
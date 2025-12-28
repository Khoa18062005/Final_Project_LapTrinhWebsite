<!-- JavaScript cho xử lý API địa chỉ -->

    // Biến lưu dữ liệu API
    let provinces = [];
    let districts = [];
    let wards = [];

    // Khi modal hiển thị
    document.getElementById('addAddressModal').addEventListener('shown.bs.modal', function () {
    loadProvinces();
});

    // Load danh sách tỉnh/thành phố từ API (chỉ tỉnh/thành)
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

    // Hiển thị loading
    const loadingOption = document.createElement('option');
    loadingOption.value = '';
    loadingOption.textContent = 'Đang tải...';
    citySelect.appendChild(loadingOption);
    citySelect.disabled = true;

    // Sử dụng API của Tỉnh thành Việt Nam - CHỈ LẤY TỈNH
    fetch('https://provinces.open-api.vn/api/')
    .then(response => {
    if (!response.ok) throw new Error('Network response was not ok');
    return response.json();
})
    .then(data => {
    provinces = data;
    citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';

    data.forEach(province => {
    const option = document.createElement('option');
    option.value = province.code;
    option.textContent = province.name;
    citySelect.appendChild(option);
});

    citySelect.disabled = false;
})
    .catch(error => {
    console.error('Lỗi khi tải danh sách tỉnh/thành:', error);
    citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';
    // Fallback với danh sách tĩnh nếu API lỗi
    loadStaticProvinces();
    citySelect.disabled = false;
});
}

    // Fallback: Danh sách tỉnh/thành tĩnh
    function loadStaticProvinces() {
    const staticProvinces = [
{ code: '01', name: 'Hà Nội' },
{ code: '79', name: 'TP. Hồ Chí Minh' },
{ code: '31', name: 'Hải Phòng' },
{ code: '48', name: 'Đà Nẵng' },
{ code: '92', name: 'Cần Thơ' },
{ code: '02', name: 'Hà Giang' },
{ code: '04', name: 'Cao Bằng' },
{ code: '06', name: 'Bắc Kạn' },
{ code: '08', name: 'Tuyên Quang' },
{ code: '10', name: 'Lào Cai' }
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
    .then(response => {
    if (!response.ok) throw new Error('Network response was not ok');
    return response.json();
})
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
    // Fallback tĩnh
    loadStaticDistricts(provinceCode);
});
});

    // Fallback quận/huyện tĩnh
    function loadStaticDistricts(provinceCode) {
    const districtSelect = document.getElementById('district');
    districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';

    // Dữ liệu tĩnh cho một số tỉnh
    const staticDistricts = {
    '01': [ // Hà Nội
{ code: '001', name: 'Quận Ba Đình' },
{ code: '002', name: 'Quận Hoàn Kiếm' },
{ code: '003', name: 'Quận Tây Hồ' },
{ code: '004', name: 'Quận Long Biên' },
{ code: '005', name: 'Quận Cầu Giấy' }
    ],
    '79': [ // TP.HCM
{ code: '760', name: 'Quận 1' },
{ code: '761', name: 'Quận 2' },
{ code: '762', name: 'Quận 3' },
{ code: '763', name: 'Quận 4' },
{ code: '764', name: 'Quận 5' }
    ],
    '48': [ // Đà Nẵng
{ code: '490', name: 'Quận Hải Châu' },
{ code: '491', name: 'Quận Thanh Khê' },
{ code: '492', name: 'Quận Sơn Trà' },
{ code: '493', name: 'Quận Ngũ Hành Sơn' }
    ]
};

    if (staticDistricts[provinceCode]) {
    districts = staticDistricts[provinceCode];
    districts.forEach(district => {
    const option = document.createElement('option');
    option.value = district.code;
    option.textContent = district.name;
    districtSelect.appendChild(option);
});
    districtSelect.disabled = false;
} else {
    districtSelect.innerHTML = '<option value="" selected disabled>Vui lòng nhập thủ công</option>';
}
}

    // Khi chọn quận/huyện
    document.getElementById('district').addEventListener('change', function() {
    const districtCode = this.value;
    const wardSelect = document.getElementById('ward');

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
    .then(response => {
    if (!response.ok) throw new Error('Network response was not ok');
    return response.json();
})
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
    // Fallback tĩnh
    loadStaticWards(districtCode);
});
});

    // Fallback phường/xã tĩnh
    function loadStaticWards(districtCode) {
    const wardSelect = document.getElementById('ward');
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';

    // Dữ liệu tĩnh cho một số quận
    const staticWards = {
    '001': [ // Ba Đình - HN
{ code: '00001', name: 'Phường Phúc Xá' },
{ code: '00002', name: 'Phường Trúc Bạch' },
{ code: '00003', name: 'Phường Vĩnh Phúc' }
    ],
    '760': [ // Quận 1 - HCM
{ code: '26701', name: 'Phường Bến Nghé' },
{ code: '26702', name: 'Phường Bến Thành' },
{ code: '26703', name: 'Phường Cầu Ông Lãnh' }
    ],
    '490': [ // Hải Châu - ĐN
{ code: '20251', name: 'Phường Hải Châu I' },
{ code: '20252', name: 'Phường Hải Châu II' },
{ code: '20253', name: 'Phường Phước Ninh' }
    ]
};

    if (staticWards[districtCode]) {
    wards = staticWards[districtCode];
    wards.forEach(ward => {
    const option = document.createElement('option');
    option.value = ward.code;
    option.textContent = ward.name;
    wardSelect.appendChild(option);
});
    wardSelect.disabled = false;
} else {
    wardSelect.innerHTML = '<option value="" selected disabled>Vui lòng nhập thủ công</option>';
}
}

    // Xử lý submit form
    function submitAddressForm() {
    const form = document.getElementById('addAddressForm');
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    // Kiểm tra xem đã chọn đủ chưa
    if (!citySelect.value || !districtSelect.value || !wardSelect.value) {
    alert('Vui lòng chọn đầy đủ tỉnh/thành phố, quận/huyện và phường/xã!');
    return;
}

    // Lấy tên thay vì code
    const selectedCity = citySelect.options[citySelect.selectedIndex]?.textContent;
    const selectedDistrict = districtSelect.options[districtSelect.selectedIndex]?.textContent;
    const selectedWard = wardSelect.options[wardSelect.selectedIndex]?.textContent;

    // Tạo các input ẩn để gửi tên thay vì code
    addHiddenInput(form, 'cityName', selectedCity);
    addHiddenInput(form, 'districtName', selectedDistrict);
    addHiddenInput(form, 'wardName', selectedWard);

    // Validate form
    if (form.checkValidity()) {
    // Hiển thị loading trên nút submit
    const submitBtn = document.querySelector('#addAddressModal .modal-footer .btn-primary');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang xử lý...';
    submitBtn.disabled = true;

    setTimeout(() => {
    form.submit();
}, 500); // Delay nhỏ để người dùng thấy loading
} else {
    // Nếu form không hợp lệ, hiển thị thông báo
    form.reportValidity();
}
}

    // Helper: Thêm input ẩn vào form
    function addHiddenInput(form, name, value) {
    // Xóa input cũ nếu có
    const oldInput = form.querySelector(`input[name="${name}"]`);
    if (oldInput) oldInput.remove();

    // Thêm input mới
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = name;
    input.value = value;
    form.appendChild(input);
}

    // Thêm sự kiện Enter để submit form
    document.getElementById('addAddressForm').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
    e.preventDefault();
    submitAddressForm();
}
});

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
});

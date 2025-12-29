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

    // Sử dụng API khác - DAPI Mien Tay
    fetch('https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            provinces = data;
            data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.Name;
                option.textContent = province.Name;
                citySelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Lỗi khi tải danh sách tỉnh/thành:', error);
            loadStaticProvinces();
        });
}

// Fallback: Danh sách tỉnh/thành tĩnh (phổ biến)
function loadStaticProvinces() {
    const staticProvinces = [
        'Hà Nội',
        'Hồ Chí Minh',
        'Hải Phòng',
        'Đà Nẵng',
        'Cần Thơ',
        'Bình Dương',
        'Đồng Nai',
        'Bà Rịa - Vũng Tàu',
        'Hải Dương',
        'Thái Nguyên',
        'Nghệ An',
        'Thanh Hóa',
        'Quảng Ninh',
        'Bắc Ninh',
        'Hưng Yên',
        'Nam Định',
        'Thái Bình',
        'Phú Thọ',
        'Vĩnh Phúc',
        'Hòa Bình',
        'Lạng Sơn',
        'Quảng Nam',
        'Bình Định',
        'Khánh Hòa',
        'Lâm Đồng',
        'Đắk Lắk',
        'An Giang',
        'Kiên Giang',
        'Cà Mau'
    ];

    const citySelect = document.getElementById('city');
    staticProvinces.forEach(province => {
        const option = document.createElement('option');
        option.value = province;
        option.textContent = province;
        citySelect.appendChild(option);
    });
}

// Khi chọn tỉnh/thành phố
document.getElementById('city').addEventListener('change', function() {
    const provinceName = this.value;
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    if (!provinceName) {
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

    // Tìm tỉnh được chọn
    const selectedProvince = provinces.find(p => p.Name === provinceName);
    if (!selectedProvince) {
        districtSelect.innerHTML = '<option value="" selected disabled>Không tìm thấy dữ liệu quận/huyện</option>';
        return;
    }

    // Lấy quận/huyện từ tỉnh đã chọn
    districts = selectedProvince.Districts || [];
    setTimeout(() => {
        districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';

        if (districts.length > 0) {
            districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.Name;
                option.textContent = district.Name;
                districtSelect.appendChild(option);
            });
            districtSelect.disabled = false;
        } else {
            districtSelect.innerHTML = '<option value="" selected disabled>Không có quận/huyện</option>';
        }
    }, 300);
});

// Khi chọn quận/huyện
document.getElementById('district').addEventListener('change', function() {
    const districtName = this.value;
    const wardSelect = document.getElementById('ward');
    const cityName = document.getElementById('city').value;

    if (!districtName) {
        wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
        wardSelect.disabled = true;
        return;
    }

    // Reset select phường/xã
    wardSelect.innerHTML = '<option value="" selected disabled>Đang tải...</option>';
    wardSelect.disabled = true;

    // Tìm tỉnh và quận được chọn
    const selectedProvince = provinces.find(p => p.Name === cityName);
    if (!selectedProvince) {
        wardSelect.innerHTML = '<option value="" selected disabled>Không tìm thấy dữ liệu phường/xã</option>';
        return;
    }

    const selectedDistrict = selectedProvince.Districts.find(d => d.Name === districtName);
    if (!selectedDistrict) {
        wardSelect.innerHTML = '<option value="" selected disabled>Không tìm thấy dữ liệu phường/xã</option>';
        return;
    }

    // Lấy phường/xã từ quận đã chọn
    wards = selectedDistrict.Wards || [];
    setTimeout(() => {
        wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';

        if (wards.length > 0) {
            wards.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.Name;
                option.textContent = ward.Name;
                wardSelect.appendChild(option);
            });
            wardSelect.disabled = false;
        } else {
            wardSelect.innerHTML = '<option value="" selected disabled>Không có phường/xã</option>';
        }
    }, 300);
});

// Reset form khi modal đóng
document.getElementById('addAddressModal').addEventListener('hidden.bs.modal', function () {
    const form = document.getElementById('addAddressForm');
    form.reset();

    // Reset các select
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';
    districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
    districtSelect.disabled = true;
    wardSelect.disabled = true;

    // Load lại tỉnh/thành phố
    setTimeout(loadProvinces, 100);
});

// Đơn giản hóa: Nếu API không hoạt động, dùng dữ liệu tĩnh cho một số tỉnh phổ biến
function loadStaticDataForCommonCities(cityName, districtSelect, wardSelect) {
    const staticData = {
        'Hà Nội': {
            districts: [
                'Quận Ba Đình', 'Quận Hoàn Kiếm', 'Quận Tây Hồ', 'Quận Long Biên', 'Quận Cầu Giấy',
                'Quận Đống Đa', 'Quận Hai Bà Trưng', 'Quận Hoàng Mai', 'Quận Thanh Xuân',
                'Huyện Từ Liêm', 'Huyện Thanh Trì', 'Huyện Gia Lâm', 'Huyện Đông Anh', 'Huyện Sóc Sơn'
            ],
            wards: {
                'Quận Ba Đình': ['Phường Phúc Xá', 'Phường Trúc Bạch', 'Phường Vĩnh Phúc', 'Phường Cống Vị'],
                'Quận Hoàn Kiếm': ['Phường Hàng Bạc', 'Phường Hàng Bài', 'Phường Hàng Bồ', 'Phường Hàng Buồm'],
                'Quận Cầu Giấy': ['Phường Dịch Vọng', 'Phường Dịch Vọng Hậu', 'Phường Mai Dịch', 'Phường Nghĩa Đô']
            }
        },
        'Hồ Chí Minh': {
            districts: [
                'Quận 1', 'Quận 2', 'Quận 3', 'Quận 4', 'Quận 5',
                'Quận 6', 'Quận 7', 'Quận 8', 'Quận 9', 'Quận 10',
                'Quận 11', 'Quận 12', 'Quận Bình Thạnh', 'Quận Gò Vấp', 'Quận Phú Nhuận'
            ],
            wards: {
                'Quận 1': ['Phường Bến Nghé', 'Phường Bến Thành', 'Phường Cầu Ông Lãnh', 'Phường Cô Giang'],
                'Quận 2': ['Phường An Khánh', 'Phường An Lợi Đông', 'Phường An Phú', 'Phường Bình An'],
                'Quận 3': ['Phường 01', 'Phường 02', 'Phường 03', 'Phường 04']
            }
        },
        'Đà Nẵng': {
            districts: [
                'Quận Hải Châu', 'Quận Thanh Khê', 'Quận Sơn Trà', 'Quận Ngũ Hành Sơn',
                'Quận Liên Chiểu', 'Quận Cẩm Lệ', 'Huyện Hòa Vang', 'Huyện Hoàng Sa'
            ],
            wards: {
                'Quận Hải Châu': ['Phường Hải Châu I', 'Phường Hải Châu II', 'Phường Phước Ninh', 'Phường Thuận Phước'],
                'Quận Thanh Khê': ['Phường An Khê', 'Phường Chính Gián', 'Phường Hòa Khê', 'Phường Tam Thuận']
            }
        }
    };

    if (staticData[cityName]) {
        districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
        staticData[cityName].districts.forEach(district => {
            const option = document.createElement('option');
            option.value = district;
            option.textContent = district;
            districtSelect.appendChild(option);
        });
        districtSelect.disabled = false;

        // Thêm sự kiện cho district để load wards
        districtSelect.onchange = function() {
            const selectedDistrict = this.value;
            if (staticData[cityName].wards[selectedDistrict]) {
                wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
                staticData[cityName].wards[selectedDistrict].forEach(ward => {
                    const option = document.createElement('option');
                    option.value = ward;
                    option.textContent = ward;
                    wardSelect.appendChild(option);
                });
                wardSelect.disabled = false;
            } else {
                wardSelect.innerHTML = '<option value="" selected disabled>Vui lòng nhập thủ công</option>';
                wardSelect.disabled = true;
            }
        };
    } else {
        districtSelect.innerHTML = '<option value="" selected disabled>Vui lòng nhập thủ công</option>';
        districtSelect.disabled = true;
    }
}

// Alternative: Dùng API backup nếu chính không hoạt động
function loadProvincesBackup() {
    const citySelect = document.getElementById('city');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');

    // Reset các select
    citySelect.innerHTML = '<option value="" selected disabled>Chọn tỉnh/thành phố</option>';
    districtSelect.innerHTML = '<option value="" selected disabled>Chọn quận/huyện</option>';
    wardSelect.innerHTML = '<option value="" selected disabled>Chọn phường/xã</option>';
    districtSelect.disabled = true;
    wardSelect.disabled = true;

    // Try multiple APIs or use static data
    const apis = [
        'https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json',
        'https://provinces.open-api.vn/api/'
    ];

    let currentApiIndex = 0;

    function tryNextApi() {
        if (currentApiIndex >= apis.length) {
            // All APIs failed, use static data
            loadStaticProvinces();
            return;
        }

        fetch(apis[currentApiIndex])
            .then(response => {
                if (!response.ok) throw new Error('API failed');
                return response.json();
            })
            .then(data => {
                if (apis[currentApiIndex].includes('open-api')) {
                    // provinces.open-api.vn format
                    provinces = data;
                    data.forEach(province => {
                        const option = document.createElement('option');
                        option.value = province.name;
                        option.textContent = province.name;
                        citySelect.appendChild(option);
                    });
                } else {
                    // kenzouno1 format
                    provinces = data;
                    data.forEach(province => {
                        const option = document.createElement('option');
                        option.value = province.Name;
                        option.textContent = province.Name;
                        citySelect.appendChild(option);
                    });
                }
            })
            .catch(error => {
                console.error(`API ${currentApiIndex + 1} failed:`, error);
                currentApiIndex++;
                tryNextApi();
            });
    }

    tryNextApi();
}
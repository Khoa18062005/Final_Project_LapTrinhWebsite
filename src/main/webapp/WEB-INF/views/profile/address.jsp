<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Địa Chỉ Của Tôi" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header d-flex justify-content-between align-items-center">
      <div>
        <h4><i class="bi bi-geo-alt me-2"></i>Địa Chỉ Của Tôi</h4>
        <p class="text-muted mb-0">Quản lý địa chỉ giao hàng của bạn</p>
      </div>
      <button type="button" class="btn btn-primary btn-add-address" data-bs-toggle="modal" data-bs-target="#addAddressModal">
        <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ mới
      </button>
    </div>

    <!-- HIỂN THỊ DANH SÁCH ĐỊA CHỈ -->
    <div class="row mt-4">
      <c:choose>
        <c:when test="${empty addresses}">
          <!-- EMPTY STATE -->
          <div class="col-12">
            <div class="empty-state text-center py-5">
              <i class="bi bi-geo-alt fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có địa chỉ nào</h5>
              <p class="text-muted">Bạn chưa thêm địa chỉ giao hàng nào</p>
              <button type="button" class="btn btn-primary btn-add-address mt-3" data-bs-toggle="modal" data-bs-target="#addAddressModal">
                <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ đầu tiên
              </button>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <!-- DANH SÁCH ĐỊA CHỈ -->
          <c:forEach var="address" items="${addresses}">
            <div class="col-md-6 mb-4">
              <div class="address-card border rounded p-4 position-relative">
                <!-- Badge mặc định -->
                <c:if test="${address.isDefault()}">
                  <span class="badge bg-danger position-absolute top-0 end-0 m-3">Mặc Định</span>
                </c:if>

                <!-- Thông tin người nhận -->
                <div class="d-flex justify-content-between align-items-start mb-3">
                  <div>
                    <h5 class="mb-1">${address.receiverName}</h5>
                    <p class="text-muted mb-0">
                      <i class="bi bi-telephone me-1"></i> ${address.phone}
                    </p>
                  </div>
                </div>

                <!-- Địa chỉ -->
                <p class="mb-3">
                  <i class="bi bi-house-door me-2"></i>
                    ${address.street}, ${address.ward}, ${address.district}, ${address.city}
                </p>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>

<!-- MODAL THÊM ĐỊA CHỈ MỚI -->
<div class="modal fade modal-top-fixed" id="addAddressModal" tabindex="-1" aria-labelledby="addAddressModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
    <form id="addAddressForm" action="${pageContext.request.contextPath}/profile/address/add" method="POST">
      <div class="modal-content">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="addAddressModalLabel">
            <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ mới
          </h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="row">
            <!-- Người nhận -->
            <div class="col-md-6 mb-3">
              <label for="receiverName" class="form-label">Tên người nhận <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="receiverName" name="receiverName" required>
            </div>

            <!-- Số điện thoại -->
            <div class="col-md-6 mb-3">
              <label for="phone" class="form-label">Số điện thoại <span class="text-danger">*</span></label>
              <input type="tel" class="form-control" id="phone" name="phone" required>
            </div>

            <!-- Tỉnh/Thành phố -->
            <div class="col-md-4 mb-3">
              <label for="city" class="form-label">Tỉnh/Thành phố <span class="text-danger">*</span></label>
              <select class="form-select" id="city" name="city" required>
                <option value="" selected disabled>Chọn tỉnh/thành phố</option>
              </select>
            </div>

            <!-- Quận/Huyện -->
            <div class="col-md-4 mb-3">
              <label for="district" class="form-label">Quận/Huyện <span class="text-danger">*</span></label>
              <select class="form-select" id="district" name="district" required disabled>
                <option value="" selected disabled>Chọn quận/huyện</option>
              </select>
            </div>

            <!-- Phường/Xã -->
            <div class="col-md-4 mb-3">
              <label for="ward" class="form-label">Phường/Xã <span class="text-danger">*</span></label>
              <select class="form-select" id="ward" name="ward" required disabled>
                <option value="" selected disabled>Chọn phường/xã</option>
              </select>
            </div>

            <!-- Đường/Số nhà -->
            <div class="col-12 mb-3">
              <label for="street" class="form-label">Đường/Số nhà <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="street" name="street" required>
            </div>

            <!-- Đặt làm mặc định -->
            <div class="col-12 mb-3">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="isDefault" name="isDefault">
                <label class="form-check-label" for="isDefault">
                  Đặt làm địa chỉ mặc định
                </label>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          <button type="submit" class="btn btn-primary">Lưu địa chỉ</button>
        </div>
      </div>
    </form>
  </div>
</div>

<!-- JavaScript đơn giản cho API -->
<!-- JavaScript đơn giản cho API -->
<script>
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
</script>

<%@ include file="components/footer.jsp" %>
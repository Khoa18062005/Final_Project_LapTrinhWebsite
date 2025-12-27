<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="section-header">
  <h2>Quản lý sản phẩm</h2>
  <button class="btn btn-primary" onclick="openAddProductModal()">
    <i class="fas fa-plus"></i> Thêm sản phẩm
  </button>
</div>
<style>
  /* ... Các CSS khác giữ nguyên ... */

  /* ========================================= */
  /* CẬP NHẬT: MODAL CHÍNH GIỮA MÀN HÌNH */
  /* ========================================= */
  .modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5); /* Nền tối mờ */
    overflow: auto; /* Cho phép cuộn nếu màn hình quá nhỏ */
  }

  .modal-content {
    background-color: #fefefe;
    padding: 25px;
    border: 1px solid #888;
    width: 90%;
    max-width: 900px; /* Độ rộng tối đa của bảng */
    border-radius: 10px;
    box-shadow: 0 5px 20px rgba(0,0,0,0.3);

    /* Kỹ thuật căn giữa tuyệt đối */
    position: relative;
    top: 50%;
    transform: translateY(-50%); /* Đẩy ngược lại 50% chiều cao để vào giữa */
    margin: 0 auto; /* Căn giữa chiều ngang */

    /* Đảm bảo bảng không bị tràn màn hình khi nội dung dài */
    max-height: 90vh;
    overflow-y: auto;
  }

  /* Tùy chỉnh thanh cuộn cho Modal đẹp hơn */
  .modal-content::-webkit-scrollbar { width: 8px; }
  .modal-content::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 4px; }
  .modal-content::-webkit-scrollbar-thumb { background: #888; border-radius: 4px; }
  .modal-content::-webkit-scrollbar-thumb:hover { background: #555; }

  /* Căn giữa tiêu đề bảng */
  .spec-table th {
    text-align: center;
    background-color: #f1f3f5;
    color: #333;
  }

  /* ... Các CSS khác giữ nguyên ... */
</style>
<div class="filter-section">
  <label><i class="fas fa-filter"></i> <strong>Lọc theo danh mục:</strong></label>
  <form action="${pageContext.request.contextPath}/admin" method="GET" id="filterForm" style="margin: 0;">
    <select name="category" onchange="document.getElementById('filterForm').submit()">
      <option value="">-- Tất cả sản phẩm --</option>
      <option value="1" ${currentCategory == 1 ? 'selected' : ''}>📱 Điện thoại</option>
      <option value="3" ${currentCategory == 3 ? 'selected' : ''}>💻 Laptop</option>
      <option value="4" ${currentCategory == 4 ? 'selected' : ''}>🖊 Tablet</option>
      <option value="5" ${currentCategory == 5 ? 'selected' : ''}>🎧 Tai nghe / Phụ kiện</option>
    </select>
  </form>
</div>

<c:if test="${not empty param.message}">
  <div style="background-color: #d4edda; color: #155724; padding: 10px; margin-bottom: 15px; border-radius: 4px; border: 1px solid #c3e6cb;">
    <i class="fas fa-check-circle"></i> ${param.message}
  </div>
</c:if>
<c:if test="${not empty param.error}">
  <div style="background-color: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px; border-radius: 4px; border: 1px solid #f5c6cb;">
    <i class="fas fa-exclamation-triangle"></i> Đã có lỗi xảy ra!
  </div>
</c:if>

<div class="table-container">
  <table class="data-table">
    <thead>
    <tr>
      <th>Mã</th>
      <th>Tên sản phẩm</th>
      <th>Người bán</th>
      <th>Danh mục</th>
      <th>Giá gốc</th>
      <th>Trạng thái</th>
      <th>Thao tác</th>
    </tr>
    </thead>
    <tbody id="productsTable">
    <c:forEach var="p" items="${productList}">
      <tr>
        <td>#${p.productId}</td>
        <td>
          <strong>${p.name}</strong>
          <br><small style="color: #888;">${p.slug}</small>
        </td>
        <td>
                    <span style="font-family: monospace; background: #f1f1f1; padding: 2px 5px; border-radius: 3px;">
                        Vendor #${p.vendorId}
                    </span>
        </td>
        <td>
          <c:choose>
            <c:when test="${p.categoryId == 1}"><span class="badge-type phone"><i class="fas fa-mobile-alt"></i> Điện thoại</span></c:when>
            <c:when test="${p.categoryId == 3}"><span class="badge-type laptop"><i class="fas fa-laptop"></i> Laptop</span></c:when>
            <c:when test="${p.categoryId == 4}"><span class="badge-type tablet"><i class="fas fa-tablet-alt"></i> Tablet</span></c:when>
            <c:when test="${p.categoryId == 5}"><span class="badge-type accessory"><i class="fas fa-headphones"></i> Tai nghe</span></c:when>
            <c:otherwise><span class="badge-type">Khác</span></c:otherwise>
          </c:choose>
        </td>
        <td>
          <fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
        </td>
        <td>
                    <span class="status-badge ${p.status == 'Active' ? 'delivered' : 'rejected'}">
                        ${p.status != null ? p.status : 'Active'}
                    </span>
        </td>
        <td>
          <div class="action-buttons">
            <button type="button" class="btn-icon view" onclick="showProductDetails('detail-${p.productId}')" title="Xem chi tiết" style="background: #17a2b8; color: white;">
              <i class="fas fa-eye"></i>
            </button>

            <button class="btn-icon edit" title="Sửa"><i class="fas fa-edit"></i></button>
            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display:inline;" onsubmit="return confirm('Xóa sản phẩm này?');">
              <input type="hidden" name="action" value="delete_product">
              <input type="hidden" name="id" value="${p.productId}">
              <button type="submit" class="btn-icon delete" title="Xóa"><i class="fas fa-trash"></i></button>
            </form>
          </div>

          <div id="detail-${p.productId}" style="display:none;">
            <div class="product-detail-header" style="border-bottom: 2px solid #f0f0f0; margin-bottom: 15px;">
              <h3 style="color: #333; margin: 0;">${p.name}</h3>
              <p style="color: #666; margin-top: 5px;">ID: #${p.productId} | SKU: ${p.slug} | Brand: ${p.brand}</p>
            </div>

            <c:if test="${p.categoryId == 1}">
              <h4 class="spec-title" style="color: #3498db; border-left: 4px solid #3498db; padding-left: 10px;">📱 Thông số Điện thoại</h4>
              <table class="spec-table" style="width: 100%; border-collapse: collapse;">
                <tr style="background:#f8f9fa"><th colspan="2">Màn hình & Cấu hình</th></tr>
                <tr><td>Màn hình</td><td>${p.screenSize} inch, ${p.screenResolution} (${p.screenType} - ${p.refreshRate}Hz)</td></tr>
                <tr><td>CPU/GPU</td><td>${p.processor} | ${p.gpu}</td></tr>
                <tr><td>Hệ điều hành</td><td>${p.os} ${p.osVersion}</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Camera & Pin</th></tr>
                <tr><td>Camera</td><td>Sau: ${p.rearCamera} <br> Trước: ${p.frontCamera}</td></tr>
                <c:if test="${not empty p.videoRecording}"><tr><td>Quay phim</td><td>${p.videoRecording}</td></tr></c:if>
                <tr><td>Pin/Sạc</td><td>${p.batteryCapacity} mAh (${p.chargingSpeed}W - ${p.chargerType})</td></tr>
                <tr><td>Sạc khác</td><td>${p.wirelessCharging ? 'Sạc không dây' : ''} ${p.reverseCharging ? ', Sạc ngược' : ''}</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Tiện ích</th></tr>
                <tr><td>Kết nối</td><td>${p.connectivity} (Sim: ${p.simType}, Network: ${p.networkSupport})</td></tr>
                <tr><td>Bảo mật</td><td>${p.fingerprintSensor ? 'Vân tay' : ''} ${p.faceRecognition ? 'FaceID' : ''}</td></tr>
                <tr><td>Khác</td><td>${p.waterproofRating} / ${p.dustproofRating} ${p.nfc ? '| NFC' : ''} ${p.audioJack ? '| Jack 3.5mm' : ''}</td></tr>
              </table>
            </c:if>

            <c:if test="${p.categoryId == 3}">
              <h4 class="spec-title" style="color: #e67e22; border-left: 4px solid #e67e22; padding-left: 10px;">💻 Thông số Laptop</h4>
              <table class="spec-table" style="width: 100%; border-collapse: collapse;">
                <tr style="background:#f8f9fa"><th colspan="2">Vi xử lý & Đồ họa</th></tr>
                <tr><td>CPU</td><td>${p.cpu} ${p.cpuGeneration} (${p.cpuSpeed} GHz)</td></tr>
                <tr><td>GPU</td><td>${p.gpu} ${p.discreteGpu ? '(Card rời)' : '(Onboard)'} <c:if test="${p.discreteGpu && p.gpuMemory > 0}">- ${p.gpuMemory}GB</c:if></td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Bộ nhớ & Lưu trữ</th></tr>
                <tr><td>RAM</td><td>${p.ram}GB ${p.ramType} (Max: ${p.maxRam}GB)</td></tr>
                <tr><td>Ổ cứng</td><td>${p.storage}GB ${p.storageType} ${p.additionalSlot ? '(Có khe cắm thêm)' : ''}</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Màn hình</th></tr>
                <tr><td>Thông số</td><td>${p.screenSize} inch ${p.screenResolution}</td></tr>
                <tr><td>Chi tiết</td><td>${p.screenType} - ${p.refreshRate}Hz (${p.colorGamut})</td></tr>
                <c:if test="${p.touchScreen}"><tr><td>Cảm ứng</td><td>Có</td></tr></c:if>

                <tr style="background:#f8f9fa"><th colspan="2">Kết nối & Tiện ích</th></tr>
                <tr><td>Cổng kết nối</td><td>${p.ports} ${p.thunderbolt ? ', Thunderbolt' : ''}</td></tr>
                <tr><td>Bàn phím</td><td>${p.keyboardType} ${p.keyboardBacklight ? '(Đèn nền)' : ''}</td></tr>
                <tr><td>Webcam/Mic</td><td>${p.webcam} / ${p.microphone ? 'Có mic' : 'Không mic'}</td></tr>
                <tr><td>Loa</td><td>${p.speakers}</td></tr>
                <tr><td>Bảo mật</td><td>${p.fingerprintSensor ? 'Cảm biến vân tay' : 'Không'}</td></tr>
                <tr><td>Pin/OS</td><td>${p.batteryCapacity} Wh (${p.batteryLife}h) | ${p.os}</td></tr>
              </table>
            </c:if>

            <c:if test="${p.categoryId == 4}">
              <h4 class="spec-title" style="color: #9b59b6; border-left: 4px solid #9b59b6; padding-left: 10px;">🖊 Thông số Tablet</h4>
              <table class="spec-table" style="width: 100%; border-collapse: collapse;">
                <tr style="background:#f8f9fa"><th colspan="2">Màn hình</th></tr>
                <tr><td>Hiển thị</td><td>${p.screenSize} inch ${p.screenResolution}</td></tr>
                <tr><td>Công nghệ</td><td>${p.screenType} - ${p.refreshRate}Hz</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Cấu hình & OS</th></tr>
                <tr><td>Chipset</td><td>CPU: ${p.processor} | GPU: ${p.gpu}</td></tr>
                <tr><td>Hệ điều hành</td><td>${p.os} ${p.osVersion}</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Camera & Quay phim</th></tr>
                <tr><td>Camera</td><td>Sau: ${p.rearCamera} | Trước: ${p.frontCamera}</td></tr>
                <c:if test="${not empty p.videoRecording}"><tr><td>Quay phim</td><td>${p.videoRecording}</td></tr></c:if>

                <tr style="background:#f8f9fa"><th colspan="2">Tính năng & Kết nối</th></tr>
                <tr><td>Kết nối mạng</td><td>${p.simSupport ? 'Có SIM' : 'Wifi Only'} (${p.networkSupport})</td></tr>
                <tr><td>Kết nối khác</td><td>${p.connectivity}</td></tr>
                <tr><td>Bút cảm ứng</td><td>${p.stylusSupport ? (p.stylusIncluded ? 'Có hỗ trợ (Kèm bút)' : 'Có hỗ trợ (Không kèm bút)') : 'Không hỗ trợ'}</td></tr>
                <tr><td>Bàn phím</td><td>${p.keyboardSupport ? 'Hỗ trợ bàn phím rời' : 'Không hỗ trợ'}</td></tr>
                <tr><td>Loa/Jack</td><td>${p.speakers} ${p.audioJack ? '| Jack 3.5mm' : ''}</td></tr>
                <tr><td>Bảo mật</td><td>${p.faceRecognition ? 'FaceID' : ''} ${p.fingerprintSensor ? 'Vân tay' : ''}</td></tr>
                <c:if test="${not empty p.waterproofRating}"><tr><td>Kháng nước</td><td>${p.waterproofRating}</td></tr></c:if>
                <tr><td>Pin</td><td>${p.batteryCapacity} mAh</td></tr>
              </table>
            </c:if>

            <c:if test="${p.categoryId == 5}">
              <h4 class="spec-title" style="color: #2ecc71; border-left: 4px solid #2ecc71; padding-left: 10px;">🎧 Thông số Tai nghe</h4>
              <table class="spec-table" style="width: 100%; border-collapse: collapse;">
                <tr style="background:#f8f9fa"><th colspan="2">Thông tin chung & Thiết kế</th></tr>
                <tr><td>Loại</td><td>${p.type} (${p.formFactor}) ${p.foldable ? '- Gập được' : ''}</td></tr>
                <c:if test="${not empty p.waterproofRating}"><tr><td>Kháng nước</td><td>${p.waterproofRating}</td></tr></c:if>
                <c:if test="${not empty p.wiredConnection}"><tr><td>Cắm dây</td><td>${p.wiredConnection}</td></tr></c:if>

                <tr style="background:#f8f9fa"><th colspan="2">Âm thanh</th></tr>
                <tr><td>Driver</td><td>${p.driverSize}mm (${p.driverType})</td></tr>
                <tr><td>Chất âm</td><td>${p.soundProfile} ${p.surroundSound ? '| Âm thanh vòm' : ''} ${p.customEQ ? '| EQ tùy chỉnh' : ''}</td></tr>
                <tr><td>Thông số</td><td>${p.frequencyResponse} | ${p.impedance}Ω | ${p.sensitivity}dB</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Kết nối</th></tr>
                <tr><td>Bluetooth</td><td>${p.bluetoothVersion} (Codecs: ${p.bluetoothCodecs})</td></tr>
                <tr><td>Tính năng</td><td>${p.connectivity} ${p.multipoint ? '| Kết nối đa điểm' : ''} ${p.appControl ? '| Có App điều khiển' : ''}</td></tr>

                <tr style="background:#f8f9fa"><th colspan="2">Pin & Tính năng</th></tr>
                <tr><td>Pin</td><td>${p.batteryLife} giờ (Sạc ${p.chargingTime} phút qua ${p.chargingPort})</td></tr>
                <tr><td>Chống ồn</td><td>${p.noiseCancellation ? 'Có' : 'Không'} <c:if test="${not empty p.noiseCancellationType}">(${p.noiseCancellationType})</c:if></td></tr>
                <tr><td>Chế độ xuyên âm</td><td>${p.ambientMode ? 'Có (Ambient Mode)' : 'Không'}</td></tr>
                <tr><td>Microphone</td><td>${p.microphone ? 'Có' : 'Không'} <c:if test="${not empty p.micType}">(${p.micType})</c:if></td></tr>
              </table>
            </c:if>

            <h4 style="margin-top: 20px; color: #555; border-bottom: 1px solid #ddd;">📦 Thông tin quản lý</h4>
            <p>
              <c:if test="${not empty p.brand}"><strong>Thương hiệu:</strong> ${p.brand} | </c:if>
              <c:if test="${not empty p.conditions}"><strong>Tình trạng:</strong> ${p.conditions}</c:if>
            </p>
            <p>
              <c:if test="${not empty p.dimensions}"><strong>Kích thước:</strong> ${p.dimensions} | </c:if>
              <c:if test="${p.weight > 0}"><strong>Trọng lượng:</strong> ${p.weight}kg</c:if>
            </p>
            <p><strong>Thống kê:</strong> Xem: ${p.viewCount} | Đã bán: ${p.totalSold} | Đánh giá: ${p.averageRating}⭐ (${p.totalReviews})</p>

            <c:if test="${not empty p.description}">
              <p><strong>Mô tả:</strong><br>${p.description}</p>
            </c:if>
          </div>
        </td>
      </tr>
    </c:forEach>

    <c:if test="${empty productList}">
      <tr><td colspan="7" style="text-align: center; padding: 20px;">Không tìm thấy sản phẩm nào.</td></tr>
    </c:if>
    </tbody>
  </table>
</div>

<div id="productModal" class="modal">
  <div class="modal-content">
    <div class="modal-header"><h2>Thêm sản phẩm mới</h2><span class="close" onclick="closeModal('productModal')">&times;</span></div>
    <div class="modal-body">
      <form id="productForm" action="${pageContext.request.contextPath}/admin" method="POST">
        <input type="hidden" name="action" value="add_product">
        <div class="form-group"><label>Tên</label><input type="text" name="name" class="form-control" required></div>
        <div class="form-group">
          <label>Danh mục</label>
          <select name="categoryId" class="form-control" required>
            <option value="1">Điện thoại</option><option value="3">Laptop</option><option value="4">Tablet</option><option value="5">Phụ kiện</option>
          </select>
        </div>
        <div class="form-row" style="display:flex;gap:10px">
          <div class="form-group" style="flex:1"><label>Giá</label><input type="number" name="price" class="form-control" required></div>
          <div class="form-group" style="flex:1"><label>Kho</label><input type="number" name="stock" class="form-control" value="10"></div>
        </div>
        <div class="form-group"><label>Mô tả</label><textarea name="description" class="form-control"></textarea></div>
        <div class="modal-footer"><button type="button" class="btn btn-secondary" onclick="closeModal('productModal')">Hủy</button><button type="submit" class="btn btn-primary">Lưu</button></div>
      </form>
    </div>
  </div>
</div>

<div id="viewDetailModal" class="modal">
  <div class="modal-content">
    <div class="modal-header"><h2>Chi tiết sản phẩm</h2><span class="close" onclick="closeModal('viewDetailModal')">&times;</span></div>
    <div class="modal-body" id="viewDetailContent" style="padding: 20px; max-height: 70vh; overflow-y: auto;"></div>
    <div class="modal-footer"><button type="button" class="btn btn-secondary" onclick="closeModal('viewDetailModal')">Đóng</button></div>
  </div>
</div>

<script>
  function showProductDetails(sourceId) {
    var sourceContent = document.getElementById(sourceId);
    if (sourceContent) {
      document.getElementById('viewDetailContent').innerHTML = sourceContent.innerHTML;
      document.getElementById('viewDetailModal').style.display = "block";
    }
  }
</script>
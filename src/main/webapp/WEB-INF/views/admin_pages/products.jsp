<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Section Header -->
<div class="section-header">
    <h2>Quản lý sản phẩm</h2>
    <button class="btn btn-primary" onclick="openAddProductModal()">
        <i class="fas fa-plus"></i> Thêm sản phẩm
    </button>
</div>

<!-- Filter Section -->
<div class="filter-section">
    <label>
        <i class="fas fa-filter"></i>
        <strong>Lọc theo danh mục:</strong>
    </label>
    <form action="${pageContext.request.contextPath}/admin" method="GET" id="filterForm" style="margin: 0;">
        <select name="category" onchange="document.getElementById('filterForm').submit()">
            <option value="">-- Tất cả sản phẩm --</option>
            <option value="1" ${currentCategory == 1 ? 'selected' : ''}>Điện thoại</option>
            <option value="3" ${currentCategory == 3 ? 'selected' : ''}>Laptop</option>
            <option value="4" ${currentCategory == 4 ? 'selected' : ''}>Tablet</option>
            <option value="5" ${currentCategory == 5 ? 'selected' : ''}>Tai nghe / Phụ kiện</option>
        </select>
    </form>
</div>

<!-- Alert Messages -->
<c:if test="${not empty param.message}">
    <div class="alert alert-success">
        <i class="fas fa-check-circle"></i> ${param.message}
    </div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i> Đã có lỗi xảy ra!
    </div>
</c:if>

<!-- Products Table -->
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
                    <td><strong>#${p.productId}</strong></td>
                    <td>
                        <div style="display: flex; flex-direction: column;">
                            <strong style="color: var(--text-primary);">${p.name}</strong>
                            <small style="color: var(--text-muted); margin-top: 4px;">${p.slug}</small>
                        </div>
                    </td>
                    <td>
                        <span style="
                            font-family: 'JetBrains Mono', 'Roboto', monospace;
                            background: var(--bg-primary, #f8f9fa);
                            padding: 4px 8px;
                            border-radius: 6px;
                            font-size: 12px;
                            color: var(--text-secondary, #666666);
                        ">
                            Vendor #${p.vendorId}
                        </span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${p.categoryId == 1}">
                                <span class="badge-type phone">
                                    <i class="fas fa-mobile-alt"></i> Điện thoại
                                </span>
                            </c:when>
                            <c:when test="${p.categoryId == 3}">
                                <span class="badge-type laptop">
                                    <i class="fas fa-laptop"></i> Laptop
                                </span>
                            </c:when>
                            <c:when test="${p.categoryId == 4}">
                                <span class="badge-type tablet">
                                    <i class="fas fa-tablet-alt"></i> Tablet
                                </span>
                            </c:when>
                            <c:when test="${p.categoryId == 5}">
                                <span class="badge-type accessory">
                                    <i class="fas fa-headphones"></i> Tai nghe
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge-type">Khác</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <strong style="color: var(--primary-color, #0d6efd);">
                            <fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </strong>
                    </td>
                    <td>
                        <span class="status-badge ${p.status == 'Active' ? 'delivered' : 'rejected'}">
                            ${p.status != null ? p.status : 'Active'}
                        </span>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <button type="button" class="btn-icon view" onclick="showProductDetails('detail-${p.productId}')" title="Xem chi tiết">
                                <i class="fas fa-info-circle"></i>
                            </button>
                            <button class="btn-icon edit" title="Chỉnh sửa" onclick="editProduct(${p.productId})">
                                <i class="fas fa-pen"></i>
                            </button>
                            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display:inline;" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">
                                <input type="hidden" name="action" value="delete_product">
                                <input type="hidden" name="id" value="${p.productId}">
                                <button type="submit" class="btn-icon delete" title="Xóa sản phẩm">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </form>
                        </div>

                        <!-- Product Details Hidden Panel -->
                        <div id="detail-${p.productId}" style="display:none;">
                            <div class="product-detail-header">
                                <h3>${p.name}</h3>
                                <p>ID: #${p.productId} | SKU: ${p.slug} | Brand: ${p.brand}</p>
                            </div>

                            <!-- Phone Specs -->
                            <c:if test="${p.categoryId == 1}">
                                <h4 class="spec-title" style="color: var(--primary-color, #0d6efd);">
                                    <i class="fas fa-mobile-alt"></i> Thông số Điện thoại
                                </h4>
                                <table class="spec-table">
                                    <tr style="background: var(--bg-primary, #f8f9fa);">
                                        <th colspan="2">Màn hình & Cấu hình</th>
                                    </tr>
                                    <tr>
                                        <td>Màn hình</td>
                                        <td>${p.screenSize} inch, ${p.screenResolution} (${p.screenType} - ${p.refreshRate}Hz)</td>
                                    </tr>
                                    <tr>
                                        <td>CPU/GPU</td>
                                        <td>${p.processor} | ${p.gpu}</td>
                                    </tr>
                                    <tr>
                                        <td>Hệ điều hành</td>
                                        <td>${p.os} ${p.osVersion}</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Camera & Pin</th>
                                    </tr>
                                    <tr>
                                        <td>Camera</td>
                                        <td>Sau: ${p.rearCamera} <br> Trước: ${p.frontCamera}</td>
                                    </tr>
                                    <c:if test="${not empty p.videoRecording}">
                                        <tr>
                                            <td>Quay phim</td>
                                            <td>${p.videoRecording}</td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td>Pin/Sạc</td>
                                        <td>${p.batteryCapacity} mAh (${p.chargingSpeed}W - ${p.chargerType})</td>
                                    </tr>
                                    <tr>
                                        <td>Sạc khác</td>
                                        <td>${p.wirelessCharging ? 'Sạc không dây' : ''} ${p.reverseCharging ? ', Sạc ngược' : ''}</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Tiện ích</th>
                                    </tr>
                                    <tr>
                                        <td>Kết nối</td>
                                        <td>${p.connectivity} (Sim: ${p.simType}, Network: ${p.networkSupport})</td>
                                    </tr>
                                    <tr>
                                        <td>Bảo mật</td>
                                        <td>${p.fingerprintSensor ? 'Vân tay' : ''} ${p.faceRecognition ? 'FaceID' : ''}</td>
                                    </tr>
                                    <tr>
                                        <td>Khác</td>
                                        <td>${p.waterproofRating} / ${p.dustproofRating} ${p.nfc ? '| NFC' : ''} ${p.audioJack ? '| Jack 3.5mm' : ''}</td>
                                    </tr>
                                </table>
                            </c:if>

                            <!-- Laptop Specs -->
                            <c:if test="${p.categoryId == 3}">
                                <h4 class="spec-title" style="color: var(--cta-500, #f59e0b);">
                                    <i class="fas fa-laptop"></i> Thông số Laptop
                                </h4>
                                <table class="spec-table">
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Vi xử lý & Đồ họa</th>
                                    </tr>
                                    <tr>
                                        <td>CPU</td>
                                        <td>${p.cpu} ${p.cpuGeneration} (${p.cpuSpeed} GHz)</td>
                                    </tr>
                                    <tr>
                                        <td>GPU</td>
                                        <td>${p.gpu} ${p.discreteGpu ? '(Card rời)' : '(Onboard)'} <c:if test="${p.discreteGpu && p.gpuMemory > 0}">- ${p.gpuMemory}GB</c:if></td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Bộ nhớ & Lưu trữ</th>
                                    </tr>
                                    <tr>
                                        <td>RAM</td>
                                        <td>${p.ram}GB ${p.ramType} (Max: ${p.maxRam}GB)</td>
                                    </tr>
                                    <tr>
                                        <td>Ổ cứng</td>
                                        <td>${p.storage}GB ${p.storageType} ${p.additionalSlot ? '(Có khe cắm thêm)' : ''}</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Màn hình</th>
                                    </tr>
                                    <tr>
                                        <td>Thông số</td>
                                        <td>${p.screenSize} inch ${p.screenResolution}</td>
                                    </tr>
                                    <tr>
                                        <td>Chi tiết</td>
                                        <td>${p.screenType} - ${p.refreshRate}Hz (${p.colorGamut})</td>
                                    </tr>
                                    <c:if test="${p.touchScreen}">
                                        <tr>
                                            <td>Cảm ứng</td>
                                            <td>Có</td>
                                        </tr>
                                    </c:if>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Kết nối & Tiện ích</th>
                                    </tr>
                                    <tr>
                                        <td>Cổng kết nối</td>
                                        <td>${p.ports} ${p.thunderbolt ? ', Thunderbolt' : ''}</td>
                                    </tr>
                                    <tr>
                                        <td>Bàn phím</td>
                                        <td>${p.keyboardType} ${p.keyboardBacklight ? '(Đèn nền)' : ''}</td>
                                    </tr>
                                    <tr>
                                        <td>Webcam/Mic</td>
                                        <td>${p.webcam} / ${p.microphone ? 'Có mic' : 'Không mic'}</td>
                                    </tr>
                                    <tr>
                                        <td>Loa</td>
                                        <td>${p.speakers}</td>
                                    </tr>
                                    <tr>
                                        <td>Bảo mật</td>
                                        <td>${p.fingerprintSensor ? 'Cảm biến vân tay' : 'Không'}</td>
                                    </tr>
                                    <tr>
                                        <td>Pin/OS</td>
                                        <td>${p.batteryCapacity} Wh (${p.batteryLife}h) | ${p.os}</td>
                                    </tr>
                                </table>
                            </c:if>

                            <!-- Tablet Specs -->
                            <c:if test="${p.categoryId == 4}">
                                <h4 class="spec-title" style="color: #A855F7;">
                                    <i class="fas fa-tablet-alt"></i> Thông số Tablet
                                </h4>
                                <table class="spec-table">
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Màn hình</th>
                                    </tr>
                                    <tr>
                                        <td>Hiển thị</td>
                                        <td>${p.screenSize} inch ${p.screenResolution}</td>
                                    </tr>
                                    <tr>
                                        <td>Công nghệ</td>
                                        <td>${p.screenType} - ${p.refreshRate}Hz</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Cấu hình & OS</th>
                                    </tr>
                                    <tr>
                                        <td>Chipset</td>
                                        <td>CPU: ${p.processor} | GPU: ${p.gpu}</td>
                                    </tr>
                                    <tr>
                                        <td>Hệ điều hành</td>
                                        <td>${p.os} ${p.osVersion}</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Camera & Quay phim</th>
                                    </tr>
                                    <tr>
                                        <td>Camera</td>
                                        <td>Sau: ${p.rearCamera} | Trước: ${p.frontCamera}</td>
                                    </tr>
                                    <c:if test="${not empty p.videoRecording}">
                                        <tr>
                                            <td>Quay phim</td>
                                            <td>${p.videoRecording}</td>
                                        </tr>
                                    </c:if>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Tính năng & Kết nối</th>
                                    </tr>
                                    <tr>
                                        <td>Kết nối mạng</td>
                                        <td>${p.simSupport ? 'Có SIM' : 'Wifi Only'} (${p.networkSupport})</td>
                                    </tr>
                                    <tr>
                                        <td>Kết nối khác</td>
                                        <td>${p.connectivity}</td>
                                    </tr>
                                    <tr>
                                        <td>Bút cảm ứng</td>
                                        <td>${p.stylusSupport ? (p.stylusIncluded ? 'Có hỗ trợ (Kèm bút)' : 'Có hỗ trợ (Không kèm bút)') : 'Không hỗ trợ'}</td>
                                    </tr>
                                    <tr>
                                        <td>Bàn phím</td>
                                        <td>${p.keyboardSupport ? 'Hỗ trợ bàn phím rời' : 'Không hỗ trợ'}</td>
                                    </tr>
                                    <tr>
                                        <td>Loa/Jack</td>
                                        <td>${p.speakers} ${p.audioJack ? '| Jack 3.5mm' : ''}</td>
                                    </tr>
                                    <tr>
                                        <td>Bảo mật</td>
                                        <td>${p.faceRecognition ? 'FaceID' : ''} ${p.fingerprintSensor ? 'Vân tay' : ''}</td>
                                    </tr>
                                    <c:if test="${not empty p.waterproofRating}">
                                        <tr>
                                            <td>Kháng nước</td>
                                            <td>${p.waterproofRating}</td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td>Pin</td>
                                        <td>${p.batteryCapacity} mAh</td>
                                    </tr>
                                </table>
                            </c:if>

                            <!-- Headphone Specs -->
                            <c:if test="${p.categoryId == 5}">
                                <h4 class="spec-title" style="color: var(--success-600, #28a745);">
                                    <i class="fas fa-headphones"></i> Thông số Tai nghe
                                </h4>
                                <table class="spec-table">
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Thông tin chung & Thiết kế</th>
                                    </tr>
                                    <tr>
                                        <td>Loại</td>
                                        <td>${p.type} (${p.formFactor}) ${p.foldable ? '- Gập được' : ''}</td>
                                    </tr>
                                    <c:if test="${not empty p.waterproofRating}">
                                        <tr>
                                            <td>Kháng nước</td>
                                            <td>${p.waterproofRating}</td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${not empty p.wiredConnection}">
                                        <tr>
                                            <td>Cắm dây</td>
                                            <td>${p.wiredConnection}</td>
                                        </tr>
                                    </c:if>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Âm thanh</th>
                                    </tr>
                                    <tr>
                                        <td>Driver</td>
                                        <td>${p.driverSize}mm (${p.driverType})</td>
                                    </tr>
                                    <tr>
                                        <td>Chất âm</td>
                                        <td>${p.soundProfile} ${p.surroundSound ? '| Âm thanh vòm' : ''} ${p.customEQ ? '| EQ tùy chỉnh' : ''}</td>
                                    </tr>
                                    <tr>
                                        <td>Thông số</td>
                                        <td>${p.frequencyResponse} | ${p.impedance}Ω | ${p.sensitivity}dB</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Kết nối</th>
                                    </tr>
                                    <tr>
                                        <td>Bluetooth</td>
                                        <td>${p.bluetoothVersion} (Codecs: ${p.bluetoothCodecs})</td>
                                    </tr>
                                    <tr>
                                        <td>Tính năng</td>
                                        <td>${p.connectivity} ${p.multipoint ? '| Kết nối đa điểm' : ''} ${p.appControl ? '| Có App điều khiển' : ''}</td>
                                    </tr>
                                    <tr style="background: var(--bg-primary);">
                                        <th colspan="2">Pin & Tính năng</th>
                                    </tr>
                                    <tr>
                                        <td>Pin</td>
                                        <td>${p.batteryLife} giờ (Sạc ${p.chargingTime} phút qua ${p.chargingPort})</td>
                                    </tr>
                                    <tr>
                                        <td>Chống ồn</td>
                                        <td>${p.noiseCancellation ? 'Có' : 'Không'} <c:if test="${not empty p.noiseCancellationType}">(${p.noiseCancellationType})</c:if></td>
                                    </tr>
                                    <tr>
                                        <td>Chế độ xuyên âm</td>
                                        <td>${p.ambientMode ? 'Có (Ambient Mode)' : 'Không'}</td>
                                    </tr>
                                    <tr>
                                        <td>Microphone</td>
                                        <td>${p.microphone ? 'Có' : 'Không'} <c:if test="${not empty p.micType}">(${p.micType})</c:if></td>
                                    </tr>
                                </table>
                            </c:if>

                            <!-- Management Info -->
                            <h4 style="margin-top: 20px; color: var(--text-secondary); border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">
                                <i class="fas fa-info-circle"></i> Thông tin quản lý
                            </h4>
                            <p style="margin-top: 12px; color: var(--text-secondary); font-size: 14px;">
                                <c:if test="${not empty p.brand}"><strong>Thương hiệu:</strong> ${p.brand} | </c:if>
                                <c:if test="${not empty p.conditions}"><strong>Tình trạng:</strong> ${p.conditions}</c:if>
                            </p>
                            <p style="color: var(--text-secondary); font-size: 14px;">
                                <c:if test="${not empty p.dimensions}"><strong>Kích thước:</strong> ${p.dimensions} | </c:if>
                                <c:if test="${p.weight > 0}"><strong>Trọng lượng:</strong> ${p.weight}kg</c:if>
                            </p>
                            <p style="color: var(--text-secondary); font-size: 14px;">
                                <strong>Thống kê:</strong>
                                Xem: ${p.viewCount} |
                                Đã bán: ${p.totalSold} |
                                Đánh giá: ${p.averageRating} <i class="fas fa-star" style="color: var(--warning-500, #ffc107);"></i> (${p.totalReviews})
                            </p>
                            <c:if test="${not empty p.description}">
                                <p style="margin-top: 12px; color: var(--text-secondary); font-size: 14px;">
                                    <strong>Mô tả:</strong><br>${p.description}
                                </p>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty productList}">
                <tr>
                    <td colspan="7" style="text-align: center; padding: 48px;">
                        <i class="fas fa-box-open" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px; display: block;"></i>
                        <p style="color: var(--text-muted); margin: 0;">Không tìm thấy sản phẩm nào.</p>
                        <button class="btn btn-primary" style="margin-top: 16px;" onclick="openAddProductModal()">
                            <i class="fas fa-plus"></i> Thêm sản phẩm đầu tiên
                        </button>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<!-- View Detail Modal -->
<div id="viewDetailModal" class="modal">
    <div class="modal-content modal-lg">
        <div class="modal-header">
            <h3 id="detailModalTitle">Chi tiết sản phẩm</h3>
            <button class="modal-close" onclick="closeModal('viewDetailModal')">&times;</button>
        </div>
        <div class="modal-body" id="viewDetailContent" style="max-height: 70vh; overflow-y: auto;"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" onclick="closeModal('viewDetailModal')">
                <i class="fas fa-times"></i> Đóng
            </button>
        </div>
    </div>
</div>

<script>
    function openAddProductModal() {
        const modal = document.getElementById('productModal');
        if (modal) {
            modal.classList.add('show');
        }
    }

    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('show');
        }
    }

    function showProductDetails(detailId) {
        const detailElement = document.getElementById(detailId);
        if (!detailElement) {
            console.error('Detail element not found:', detailId);
            return;
        }

        // Get product name from detail content
        const productName = detailElement.querySelector('h3')?.textContent || 'Chi tiết sản phẩm';

        // Copy content to modal
        const modalContent = document.getElementById('viewDetailContent');
        const modalTitle = document.getElementById('detailModalTitle');

        if (modalContent && modalTitle) {
            modalTitle.textContent = productName;
            modalContent.innerHTML = detailElement.innerHTML;

            // Show modal
            document.getElementById('viewDetailModal').classList.add('show');
        }
    }

    function editProduct(productId) {
        // Redirect to edit page or open edit modal
        // For now, show alert - you can implement actual edit functionality
        alert('Chức năng chỉnh sửa sản phẩm #' + productId + ' đang được phát triển.');
        // Optionally redirect: window.location.href = contextPath + '/admin/product/edit?id=' + productId;
    }

    // Close modal when clicking outside
    document.addEventListener('DOMContentLoaded', function() {
        const modal = document.getElementById('viewDetailModal');
        if (modal) {
            modal.addEventListener('click', function(e) {
                if (e.target === this) {
                    closeModal('viewDetailModal');
                }
            });
        }
    });
</script>


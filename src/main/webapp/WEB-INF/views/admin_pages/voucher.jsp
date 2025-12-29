<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="section-header">
    <h2>Quản lý Voucher</h2>
    <button class="btn btn-primary" onclick="openAddVoucherModal()">
        <i class="fas fa-plus"></i> Thêm Voucher
    </button>
</div>

<!-- Thống kê -->
<div class="stats-grid" style="margin-bottom: 20px;">
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon purple"><i class="fas fa-ticket-alt"></i></div>
        </div>
        <div class="stat-details">
            <h3>Tổng Voucher</h3>
            <p class="stat-number">${voucherList.size()}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon green"><i class="fas fa-check-circle"></i></div>
        </div>
        <div class="stat-details">
            <h3>Đang hoạt động</h3>
            <p class="stat-number">
                <c:set var="activeCount" value="0"/>
                <c:forEach var="v" items="${voucherList}">
                    <c:if test="${v.active}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if>
                </c:forEach>
                ${activeCount}
            </p>
        </div>
    </div>
</div>

<!-- Bảng danh sách Voucher -->
<div class="table-container">
    <table class="data-table">
        <thead>
            <tr>
                <th>Mã</th>
                <th>Tên Voucher</th>
                <th>Loại</th>
                <th>Giảm giá</th>
                <th>Thời hạn</th>
                <th>Sử dụng</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="voucher" items="${voucherList}">
                <tr>
                    <td><span class="voucher-code">${voucher.code}</span></td>
                    <td>${voucher.name}</td>
                    <td>
                        <span class="badge ${voucher.type == 'percent' ? 'badge-info' : 'badge-warning'}">
                            ${voucher.type == 'percent' ? 'Phần trăm' : 'Số tiền'}
                        </span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${voucher.type == 'percent'}">
                                ${voucher.discountPercent}%
                                <c:if test="${voucher.maxDiscount > 0}">
                                    <br><small>(Tối đa: <fmt:formatNumber value="${voucher.maxDiscount}" type="number"/>đ)</small>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber value="${voucher.discountAmount}" type="number"/>đ
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <small>
                            <fmt:formatDate value="${voucher.startDate}" pattern="dd/MM/yyyy"/>
                            -
                            <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy"/>
                        </small>
                    </td>
                    <td>${voucher.usageCount}/${voucher.usageLimit}</td>
                    <td>
                        <label class="switch">
                            <input type="checkbox" ${voucher.active ? 'checked' : ''}
                                   onchange="toggleVoucherStatus(${voucher.voucherId}, this)">
                            <span class="slider round"></span>
                        </label>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <button class="btn-icon btn-edit" onclick="openEditVoucherModal(${voucher.voucherId})" title="Sửa">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn-icon btn-delete" onclick="deleteVoucher(${voucher.voucherId}, '${voucher.code}')" title="Xóa">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty voucherList}">
                <tr>
                    <td colspan="8" style="text-align: center; padding: 40px;">
                        <i class="fas fa-ticket-alt" style="font-size: 48px; color: #ccc; margin-bottom: 10px;"></i>
                        <p>Chưa có voucher nào. Hãy thêm voucher mới!</p>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<!-- Modal Thêm/Sửa Voucher -->
<div id="voucherModal" class="modal">
    <div class="modal-content modal-lg">
        <div class="modal-header">
            <h3 id="voucherModalTitle">Thêm Voucher mới</h3>
            <button class="modal-close" onclick="closeVoucherModal()">&times;</button>
        </div>
        <form id="voucherForm" onsubmit="submitVoucherForm(event)">
            <input type="hidden" id="voucherId" name="voucherId">
            <input type="hidden" id="formAction" name="action" value="add">

            <div class="modal-body">
                <div class="form-row">
                    <div class="form-group">
                        <label for="code">Mã Voucher <span class="required">*</span></label>
                        <input type="text" id="code" name="code" required placeholder="VD: SALE50"
                               style="text-transform: uppercase;">
                    </div>
                    <div class="form-group">
                        <label for="name">Tên Voucher <span class="required">*</span></label>
                        <input type="text" id="name" name="name" required placeholder="VD: Giảm 50% đơn hàng">
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" rows="2"
                              placeholder="Mô tả chi tiết voucher..."></textarea>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="type">Loại giảm giá <span class="required">*</span></label>
                        <select id="type" name="type" required onchange="toggleDiscountFields()">
                            <option value="percent">Phần trăm (%)</option>
                            <option value="fixed">Số tiền cố định (VNĐ)</option>
                        </select>
                    </div>
                    <div class="form-group" id="discountPercentGroup">
                        <label for="discountPercent">Phần trăm giảm (%)</label>
                        <input type="number" id="discountPercent" name="discountPercent"
                               min="0" max="100" step="0.1" placeholder="VD: 10">
                    </div>
                    <div class="form-group" id="discountAmountGroup" style="display: none;">
                        <label for="discountAmount">Số tiền giảm (VNĐ)</label>
                        <input type="number" id="discountAmount" name="discountAmount"
                               min="0" step="1000" placeholder="VD: 50000">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="maxDiscount">Giảm tối đa (VNĐ)</label>
                        <input type="number" id="maxDiscount" name="maxDiscount"
                               min="0" step="1000" placeholder="VD: 100000">
                    </div>
                    <div class="form-group">
                        <label for="minOrderValue">Đơn tối thiểu (VNĐ)</label>
                        <input type="number" id="minOrderValue" name="minOrderValue"
                               min="0" step="1000" placeholder="VD: 200000">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="startDate">Ngày bắt đầu <span class="required">*</span></label>
                        <input type="datetime-local" id="startDate" name="startDate" required>
                    </div>
                    <div class="form-group">
                        <label for="expiryDate">Ngày kết thúc <span class="required">*</span></label>
                        <input type="datetime-local" id="expiryDate" name="expiryDate" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="usageLimit">Giới hạn sử dụng</label>
                        <input type="number" id="usageLimit" name="usageLimit"
                               min="1" value="100" placeholder="Số lần tối đa">
                    </div>
                    <div class="form-group">
                        <label for="usageLimitPerUser">Giới hạn/người</label>
                        <input type="number" id="usageLimitPerUser" name="usageLimitPerUser"
                               min="1" value="1" placeholder="Số lần/người">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" id="isActive" name="isActive" checked>
                            <span>Kích hoạt ngay</span>
                        </label>
                    </div>
                    <div class="form-group checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" id="isPublic" name="isPublic" checked>
                            <span>Công khai</span>
                        </label>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeVoucherModal()">Hủy</button>
                <button type="submit" class="btn btn-primary" id="voucherSubmitBtn">
                    <i class="fas fa-save"></i> Lưu Voucher
                </button>
            </div>
        </form>
    </div>
</div>

<style>
/* Voucher specific styles */
.voucher-code {
    font-family: 'Courier New', monospace;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 4px 10px;
    border-radius: 4px;
    font-weight: bold;
    font-size: 12px;
}

.badge {
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 11px;
    font-weight: 600;
}

.badge-info {
    background: #e3f2fd;
    color: #1976d2;
}

.badge-warning {
    background: #fff3e0;
    color: #f57c00;
}

/* Switch toggle */
.switch {
    position: relative;
    display: inline-block;
    width: 44px;
    height: 24px;
}

.switch input {
    opacity: 0;
    width: 0;
    height: 0;
}

.slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: .3s;
}

.slider:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    transition: .3s;
}

input:checked + .slider {
    background-color: #4CAF50;
}

input:checked + .slider:before {
    transform: translateX(20px);
}

.slider.round {
    border-radius: 24px;
}

.slider.round:before {
    border-radius: 50%;
}

/* Action buttons */
.action-buttons {
    display: flex;
    gap: 8px;
    justify-content: center;
}

.btn-icon {
    width: 32px;
    height: 32px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.btn-edit {
    background: #e3f2fd;
    color: #1976d2;
}

.btn-edit:hover {
    background: #1976d2;
    color: white;
}

.btn-delete {
    background: #ffebee;
    color: #d32f2f;
}

.btn-delete:hover {
    background: #d32f2f;
    color: white;
}

/* Modal styles */
.modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    animation: fadeIn 0.3s;
}

.modal.show {
    display: flex;
    align-items: center;
    justify-content: center;
}

@keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
}

.modal-content {
    background: white;
    border-radius: 12px;
    width: 90%;
    max-width: 600px;
    max-height: 90vh;
    overflow-y: auto;
    animation: slideIn 0.3s;
}

.modal-lg {
    max-width: 700px;
}

@keyframes slideIn {
    from { transform: translateY(-50px); opacity: 0; }
    to { transform: translateY(0); opacity: 1; }
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #eee;
}

.modal-header h3 {
    margin: 0;
    color: #333;
}

.modal-close {
    background: none;
    border: none;
    font-size: 24px;
    cursor: pointer;
    color: #666;
    transition: color 0.2s;
}

.modal-close:hover {
    color: #d32f2f;
}

.modal-body {
    padding: 20px;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding: 15px 20px;
    border-top: 1px solid #eee;
    background: #f8f9fa;
    border-radius: 0 0 12px 12px;
}

/* Form styles */
.form-row {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
    margin-bottom: 15px;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 6px;
    font-weight: 600;
    color: #333;
    font-size: 14px;
}

.required {
    color: #d32f2f;
}

.form-group input,
.form-group select,
.form-group textarea {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 14px;
    transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.checkbox-group {
    display: flex;
    align-items: center;
}

.checkbox-label {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    font-weight: normal !important;
}

.checkbox-label input[type="checkbox"] {
    width: 18px;
    height: 18px;
    cursor: pointer;
}

/* Buttons */
.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    transition: all 0.2s;
}

.btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
    background: #e0e0e0;
    color: #333;
}

.btn-secondary:hover {
    background: #d0d0d0;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.section-header h2 {
    margin: 0;
    color: #333;
}

/* Stats card purple */
.stat-icon.purple {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
</style>

<script>
const contextPath = '${pageContext.request.contextPath}';

// Mở modal thêm voucher
function openAddVoucherModal() {
    document.getElementById('voucherModalTitle').textContent = 'Thêm Voucher mới';
    document.getElementById('formAction').value = 'add';
    document.getElementById('voucherForm').reset();
    document.getElementById('voucherId').value = '';

    // Set default dates
    const now = new Date();
    const nextMonth = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000);
    document.getElementById('startDate').value = formatDateTimeLocal(now);
    document.getElementById('expiryDate').value = formatDateTimeLocal(nextMonth);

    toggleDiscountFields();
    document.getElementById('voucherModal').classList.add('show');
}

// Mở modal sửa voucher
function openEditVoucherModal(voucherId) {
    document.getElementById('voucherModalTitle').textContent = 'Sửa Voucher';
    document.getElementById('formAction').value = 'update';

    // Fetch voucher data
    fetch(contextPath + '/admin/voucher?action=get&id=' + voucherId)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const v = data.voucher;
                document.getElementById('voucherId').value = v.voucherId;
                document.getElementById('code').value = v.code;
                document.getElementById('name').value = v.name;
                document.getElementById('description').value = v.description || '';
                document.getElementById('type').value = v.type;
                document.getElementById('discountPercent').value = v.discountPercent || '';
                document.getElementById('discountAmount').value = v.discountAmount || '';
                document.getElementById('maxDiscount').value = v.maxDiscount || '';
                document.getElementById('minOrderValue').value = v.minOrderValue || '';
                document.getElementById('startDate').value = v.startDate || '';
                document.getElementById('expiryDate').value = v.expiryDate || '';
                document.getElementById('usageLimit').value = v.usageLimit;
                document.getElementById('usageLimitPerUser').value = v.usageLimitPerUser;
                document.getElementById('isActive').checked = v.isActive;
                document.getElementById('isPublic').checked = v.isPublic;

                toggleDiscountFields();
                document.getElementById('voucherModal').classList.add('show');
            } else {
                alert('Lỗi: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Không thể tải thông tin voucher!');
        });
}

// Đóng modal
function closeVoucherModal() {
    document.getElementById('voucherModal').classList.remove('show');
}

// Toggle discount fields based on type
function toggleDiscountFields() {
    const type = document.getElementById('type').value;
    document.getElementById('discountPercentGroup').style.display = type === 'percent' ? 'block' : 'none';
    document.getElementById('discountAmountGroup').style.display = type === 'fixed' ? 'block' : 'none';
}

// Submit form
function submitVoucherForm(event) {
    event.preventDefault();

    const form = document.getElementById('voucherForm');
    const formData = new FormData(form);

    // Convert checkbox values
    formData.set('isActive', document.getElementById('isActive').checked ? 'true' : 'false');
    formData.set('isPublic', document.getElementById('isPublic').checked ? 'true' : 'false');

    fetch(contextPath + '/admin/voucher', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            closeVoucherModal();
            location.reload();
        } else {
            alert('Lỗi: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
    });
}

// Toggle voucher status
function toggleVoucherStatus(voucherId, checkbox) {
    fetch(contextPath + '/admin/voucher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'action=toggle_status&id=' + voucherId
    })
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            alert('Lỗi: ' + data.message);
            checkbox.checked = !checkbox.checked;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        checkbox.checked = !checkbox.checked;
    });
}

// Delete voucher
function deleteVoucher(voucherId, code) {
    if (!confirm('Bạn có chắc muốn xóa voucher "' + code + '"?')) {
        return;
    }

    fetch(contextPath + '/admin/voucher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'action=delete&id=' + voucherId
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            location.reload();
        } else {
            alert('Lỗi: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra!');
    });
}

// Helper: Format date for datetime-local input
function formatDateTimeLocal(date) {
    const pad = (n) => n.toString().padStart(2, '0');
    return date.getFullYear() + '-' +
           pad(date.getMonth() + 1) + '-' +
           pad(date.getDate()) + 'T' +
           pad(date.getHours()) + ':' +
           pad(date.getMinutes());
}

// Close modal when clicking outside
document.getElementById('voucherModal').addEventListener('click', function(e) {
    if (e.target === this) {
        closeVoucherModal();
    }
});
</script>


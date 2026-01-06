<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Section Header -->
<div class="section-header">
    <h2>Quản lý Voucher</h2>
    <button class="btn btn-primary" onclick="openAddVoucherModal()">
        <i class="fas fa-plus"></i> Thêm Voucher
    </button>
</div>

<!-- Stats Grid -->
<div class="stats-grid" style="margin-bottom: 24px;">
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon purple">
                <i class="fas fa-ticket-alt"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Tổng Voucher</h3>
            <p class="stat-number">${voucherList.size()}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon green">
                <i class="fas fa-check-circle"></i>
            </div>
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
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon orange">
                <i class="fas fa-clock"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Sắp hết hạn</h3>
            <p class="stat-number">0</p>
        </div>
    </div>
</div>

<!-- Voucher Table -->
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
                    <td>
                        <span class="voucher-code">${voucher.code}</span>
                    </td>
                    <td>
                        <strong>${voucher.name}</strong>
                    </td>
                    <td>
                        <span class="badge ${voucher.type == 'percent' ? 'badge-info' : 'badge-warning'}">
                            <i class="fas ${voucher.type == 'percent' ? 'fa-percent' : 'fa-dollar-sign'}"></i>
                            ${voucher.type == 'percent' ? 'Phần trăm' : 'Số tiền'}
                        </span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${voucher.type == 'percent'}">
                                <strong style="color: var(--primary-color, #0d6efd);">${voucher.discountPercent}%</strong>
                                <c:if test="${voucher.maxDiscount > 0}">
                                    <br><small style="color: var(--text-muted);">Tối đa: <fmt:formatNumber value="${voucher.maxDiscount}" type="number"/>đ</small>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <strong style="color: var(--primary-color, #0d6efd);"><fmt:formatNumber value="${voucher.discountAmount}" type="number"/>đ</strong>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div style="font-size: 13px; color: var(--text-secondary);">
                            <i class="fas fa-calendar-alt" style="color: var(--success, #28a745);"></i>
                            <fmt:formatDate value="${voucher.startDate}" pattern="dd/MM/yyyy"/>
                            <br>
                            <i class="fas fa-calendar-times" style="color: var(--danger, #dc3545);"></i>
                            <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy"/>
                        </div>
                    </td>
                    <td>
                        <div style="display: flex; align-items: center; gap: 8px;">
                            <div style="
                                flex: 1;
                                height: 6px;
                                background: var(--border-color, #e0e0e0);
                                border-radius: 20px;
                                overflow: hidden;
                            ">
                                <div style="
                                    width: ${voucher.usageLimit > 0 ? (voucher.usageCount / voucher.usageLimit * 100) : 0}%;
                                    height: 100%;
                                    background: ${voucher.usageCount >= voucher.usageLimit ? '#dc3545' : '#28a745'};
                                    border-radius: 20px;
                                    transition: width 0.3s ease;
                                "></div>
                            </div>
                            <span style="font-size: 13px; font-weight: 600; color: var(--text-secondary);">
                                ${voucher.usageCount}/${voucher.usageLimit}
                            </span>
                        </div>
                    </td>
                    <td>
                        <label class="switch">
                            <input type="checkbox" ${voucher.active ? 'checked' : ''} onchange="toggleVoucherStatus(${voucher.voucherId}, this)">
                            <span class="slider round"></span>
                        </label>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <button class="btn-icon edit" onclick="openEditVoucherModal(${voucher.voucherId})" title="Chỉnh sửa">
                                <i class="fas fa-pen"></i>
                            </button>
                            <button class="btn-icon delete" onclick="deleteVoucher(${voucher.voucherId}, '${voucher.code}')" title="Xóa voucher">
                                <i class="fas fa-trash-alt"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty voucherList}">
                <tr>
                    <td colspan="8" style="text-align: center; padding: 48px;">
                        <i class="fas fa-ticket-alt" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px; display: block;"></i>
                        <p style="color: var(--text-muted); margin: 0;">Chưa có voucher nào. Hãy thêm voucher mới!</p>
                        <button class="btn btn-primary" style="margin-top: 16px;" onclick="openAddVoucherModal()">
                            <i class="fas fa-plus"></i> Tạo Voucher đầu tiên
                        </button>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<!-- Add/Edit Voucher Modal -->
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
                <!-- Basic Info -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="code">Mã Voucher <span class="required">*</span></label>
                        <input type="text" id="code" name="code" class="form-control" required
                               placeholder="VD: SALE50" style="text-transform: uppercase;">
                    </div>
                    <div class="form-group">
                        <label for="name">Tên Voucher <span class="required">*</span></label>
                        <input type="text" id="name" name="name" class="form-control" required
                               placeholder="VD: Giảm 50% đơn hàng">
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" class="form-control" rows="2"
                              placeholder="Mô tả chi tiết voucher..."></textarea>
                </div>

                <!-- Discount Settings -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="type">Loại giảm giá <span class="required">*</span></label>
                        <select id="type" name="type" class="form-control" required onchange="toggleDiscountFields()">
                            <option value="percent">Phần trăm (%)</option>
                            <option value="fixed">Số tiền cố định (VNĐ)</option>
                        </select>
                    </div>
                    <div class="form-group" id="discountPercentGroup">
                        <label for="discountPercent">Phần trăm giảm (%)</label>
                        <input type="number" id="discountPercent" name="discountPercent" class="form-control"
                               min="0" max="100" step="0.1" placeholder="VD: 10">
                    </div>
                    <div class="form-group" id="discountAmountGroup" style="display: none;">
                        <label for="discountAmount">Số tiền giảm (VNĐ)</label>
                        <input type="number" id="discountAmount" name="discountAmount" class="form-control"
                               min="0" step="1000" placeholder="VD: 50000">
                    </div>
                </div>

                <!-- Limits -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="maxDiscount">Giảm tối đa (VNĐ)</label>
                        <input type="number" id="maxDiscount" name="maxDiscount" class="form-control"
                               min="0" step="1000" placeholder="VD: 100000">
                    </div>
                    <div class="form-group">
                        <label for="minOrderValue">Đơn tối thiểu (VNĐ)</label>
                        <input type="number" id="minOrderValue" name="minOrderValue" class="form-control"
                               min="0" step="1000" placeholder="VD: 200000">
                    </div>
                </div>

                <!-- Date Range -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="startDate">Ngày bắt đầu <span class="required">*</span></label>
                        <input type="datetime-local" id="startDate" name="startDate" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="expiryDate">Ngày kết thúc <span class="required">*</span></label>
                        <input type="datetime-local" id="expiryDate" name="expiryDate" class="form-control" required>
                    </div>
                </div>

                <!-- Usage Limits -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="usageLimit">Giới hạn sử dụng</label>
                        <input type="number" id="usageLimit" name="usageLimit" class="form-control"
                               min="1" value="100" placeholder="Số lần tối đa">
                    </div>
                    <div class="form-group">
                        <label for="usageLimitPerUser">Giới hạn/người</label>
                        <input type="number" id="usageLimitPerUser" name="usageLimitPerUser" class="form-control"
                               min="1" value="1" placeholder="Số lần/người">
                    </div>
                </div>

                <!-- Checkboxes -->
                <div class="form-row">
                    <div class="form-group" style="display: flex; align-items: center; gap: 12px;">
                        <label style="display: flex; align-items: center; gap: 8px; cursor: pointer; margin: 0;">
                            <input type="checkbox" id="isActive" name="isActive" checked style="width: 18px; height: 18px;">
                            <span>Kích hoạt ngay</span>
                        </label>
                    </div>
                    <div class="form-group" style="display: flex; align-items: center; gap: 12px;">
                        <label style="display: flex; align-items: center; gap: 8px; cursor: pointer; margin: 0;">
                            <input type="checkbox" id="isPublic" name="isPublic" checked style="width: 18px; height: 18px;">
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

<script>
const contextPath = '${pageContext.request.contextPath}';

// Open Add Voucher Modal
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

// Open Edit Voucher Modal
function openEditVoucherModal(voucherId) {
    document.getElementById('voucherModalTitle').textContent = 'Sửa Voucher';
    document.getElementById('formAction').value = 'update';

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

// Close Modal
function closeVoucherModal() {
    document.getElementById('voucherModal').classList.remove('show');
}

// Toggle Discount Fields
function toggleDiscountFields() {
    const type = document.getElementById('type').value;
    document.getElementById('discountPercentGroup').style.display = type === 'percent' ? 'block' : 'none';
    document.getElementById('discountAmountGroup').style.display = type === 'fixed' ? 'block' : 'none';
}

// Submit Form
function submitVoucherForm(event) {
    event.preventDefault();

    const form = document.getElementById('voucherForm');
    const formData = new FormData(form);

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

// Toggle Voucher Status
function toggleVoucherStatus(voucherId, checkbox) {
    fetch(contextPath + '/admin/voucher', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
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

// Delete Voucher
function deleteVoucher(voucherId, code) {
    if (!confirm('Bạn có chắc muốn xóa voucher "' + code + '"?')) return;

    fetch(contextPath + '/admin/voucher', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
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

// Format Date for datetime-local input
function formatDateTimeLocal(date) {
    const pad = (n) => n.toString().padStart(2, '0');
    return date.getFullYear() + '-' +
           pad(date.getMonth() + 1) + '-' +
           pad(date.getDate()) + 'T' +
           pad(date.getHours()) + ':' +
           pad(date.getMinutes());
}

// Close modal when clicking outside
document.getElementById('voucherModal')?.addEventListener('click', function(e) {
    if (e.target === this) closeVoucherModal();
});
</script>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Section Header -->
<div class="section-header">
    <h2>Quản lý đơn hàng</h2>
    <div class="header-actions">
        <button class="btn btn-secondary" onclick="exportOrders()">
            <i class="fas fa-file-export"></i> Xuất Excel
        </button>
    </div>
</div>

<!-- Filter Section -->
<div class="filter-section" style="display: flex; gap: 16px; flex-wrap: wrap; margin-bottom: 20px;">
    <form action="${pageContext.request.contextPath}/admin" method="GET" id="orderFilterForm" style="display: flex; gap: 12px; flex-wrap: wrap; align-items: center;">
        <input type="hidden" name="section" value="orders">

        <div class="filter-group">
            <label><i class="fas fa-filter"></i> Trạng thái:</label>
            <select name="orderStatus" onchange="document.getElementById('orderFilterForm').submit()">
                <option value="">-- Tất cả --</option>
                <option value="PENDING" ${param.orderStatus == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                <option value="CONFIRMED" ${param.orderStatus == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                <option value="SHIPPING" ${param.orderStatus == 'SHIPPING' ? 'selected' : ''}>Đang giao</option>
                <option value="COMPLETED" ${param.orderStatus == 'COMPLETED' ? 'selected' : ''}>Hoàn thành</option>
                <option value="CANCELLED" ${param.orderStatus == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
            </select>
        </div>

        <div class="filter-group">
            <label><i class="fas fa-calendar"></i> Từ ngày:</label>
            <input type="date" name="fromDate" value="${param.fromDate}" onchange="document.getElementById('orderFilterForm').submit()">
        </div>

        <div class="filter-group">
            <label>Đến ngày:</label>
            <input type="date" name="toDate" value="${param.toDate}" onchange="document.getElementById('orderFilterForm').submit()">
        </div>

        <div class="filter-group">
            <label><i class="fas fa-search"></i></label>
            <input type="text" name="searchOrder" placeholder="Tìm mã đơn, khách hàng..." value="${param.searchOrder}">
            <button type="submit" class="btn btn-sm btn-primary">
                <i class="fas fa-search"></i>
            </button>
        </div>
    </form>
</div>

<!-- Order Stats Cards -->
<div class="stats-grid" style="grid-template-columns: repeat(5, 1fr); margin-bottom: 24px;">
    <div class="stat-card mini">
        <div class="stat-icon orange"><i class="fas fa-clock"></i></div>
        <div class="stat-info">
            <span class="stat-number">${pendingOrders != null ? pendingOrders : 0}</span>
            <span class="stat-label">Chờ duyệt</span>
        </div>
    </div>
    <div class="stat-card mini">
        <div class="stat-icon blue"><i class="fas fa-check-circle"></i></div>
        <div class="stat-info">
            <span class="stat-number">${confirmedOrders != null ? confirmedOrders : 0}</span>
            <span class="stat-label">Đã xác nhận</span>
        </div>
    </div>
    <div class="stat-card mini">
        <div class="stat-icon purple"><i class="fas fa-shipping-fast"></i></div>
        <div class="stat-info">
            <span class="stat-number">${shippingOrders != null ? shippingOrders : 0}</span>
            <span class="stat-label">Đang giao</span>
        </div>
    </div>
    <div class="stat-card mini">
        <div class="stat-icon green"><i class="fas fa-check-double"></i></div>
        <div class="stat-info">
            <span class="stat-number">${completedOrders != null ? completedOrders : 0}</span>
            <span class="stat-label">Hoàn thành</span>
        </div>
    </div>
    <div class="stat-card mini">
        <div class="stat-icon red"><i class="fas fa-times-circle"></i></div>
        <div class="stat-info">
            <span class="stat-number">${cancelledOrders != null ? cancelledOrders : 0}</span>
            <span class="stat-label">Đã hủy</span>
        </div>
    </div>
</div>

<!-- Alert Messages -->
<c:if test="${not empty param.orderMessage}">
    <div class="alert alert-success">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${param.orderMessage == 'status_updated'}">Cập nhật trạng thái đơn hàng thành công!</c:when>
            <c:otherwise>${param.orderMessage}</c:otherwise>
        </c:choose>
    </div>
</c:if>
<c:if test="${not empty param.orderError}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i> Đã có lỗi xảy ra: ${param.orderError}
    </div>
</c:if>

<!-- Orders Table -->
<div class="table-container">
    <table class="data-table">
        <thead>
            <tr>
                <th><input type="checkbox" id="selectAllOrders" onclick="toggleSelectAllOrders()"></th>
                <th>Mã đơn</th>
                <th>Khách hàng</th>
                <th>Ngày đặt</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Thanh toán</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody id="ordersTableBody">
            <c:forEach var="order" items="${orderList}">
                <tr data-order-id="${order.orderId}">
                    <td><input type="checkbox" class="order-checkbox" value="${order.orderId}"></td>
                    <td>
                        <strong class="order-number">#${order.orderNumber}</strong>
                        <br><small style="color: var(--text-muted);">ID: ${order.orderId}</small>
                    </td>
                    <td>
                        <div class="customer-info">
                            <img src="https://ui-avatars.com/api/?name=Customer&background=random&size=32"
                                 alt="Customer" class="customer-avatar">
                            <div>
                                <strong>Khách hàng #${order.customerId}</strong>
                            </div>
                        </div>
                    </td>
                    <td>
                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/>
                        <br><small style="color: var(--text-muted);"><fmt:formatDate value="${order.orderDate}" pattern="HH:mm"/></small>
                    </td>
                    <td>
                        <strong style="color: var(--primary-color);">
                            <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </strong>
                    </td>
                    <td>
                        <c:set var="orderStatus" value="${not empty order.status ? order.status : 'UNKNOWN'}"/>
                        <span class="status-badge status-${fn:toLowerCase(orderStatus)}">
                            <c:choose>
                                <c:when test="${orderStatus == 'PENDING'}"><i class="fas fa-clock"></i> Chờ duyệt</c:when>
                                <c:when test="${orderStatus == 'CONFIRMED'}"><i class="fas fa-check"></i> Đã xác nhận</c:when>
                                <c:when test="${orderStatus == 'SHIPPING'}"><i class="fas fa-truck"></i> Đang giao</c:when>
                                <c:when test="${orderStatus == 'COMPLETED'}"><i class="fas fa-check-double"></i> Hoàn thành</c:when>
                                <c:when test="${orderStatus == 'CANCELLED'}"><i class="fas fa-times"></i> Đã hủy</c:when>
                                <c:otherwise>${orderStatus}</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>
                        <span class="badge badge-warning"><i class="fas fa-clock"></i> COD</span>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <button class="btn-icon view" title="Xem chi tiết" onclick="viewOrderDetail(${order.orderId})">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="btn-icon edit" title="Cập nhật trạng thái" onclick="openUpdateStatusModal(${order.orderId}, '${order.status}', '${order.orderNumber}')">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn-icon print" title="In hóa đơn" onclick="printInvoice(${order.orderId})">
                                <i class="fas fa-print"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty orderList}">
                <tr>
                    <td colspan="8" style="text-align: center; padding: 48px;">
                        <i class="fas fa-shopping-cart" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px; display: block;"></i>
                        <p style="color: var(--text-muted);">Không tìm thấy đơn hàng nào</p>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<!-- Pagination -->
<c:if test="${totalOrderPages > 1}">
    <div class="pagination">
        <c:if test="${currentOrderPage > 1}">
            <a href="${pageContext.request.contextPath}/admin?section=orders&page=${currentOrderPage - 1}&orderStatus=${param.orderStatus}" class="page-link">
                <i class="fas fa-chevron-left"></i>
            </a>
        </c:if>

        <c:forEach begin="1" end="${totalOrderPages}" var="i">
            <a href="${pageContext.request.contextPath}/admin?section=orders&page=${i}&orderStatus=${param.orderStatus}"
               class="page-link ${currentOrderPage == i ? 'active' : ''}">${i}</a>
        </c:forEach>

        <c:if test="${currentOrderPage < totalOrderPages}">
            <a href="${pageContext.request.contextPath}/admin?section=orders&page=${currentOrderPage + 1}&orderStatus=${param.orderStatus}" class="page-link">
                <i class="fas fa-chevron-right"></i>
            </a>
        </c:if>
    </div>
</c:if>

<!-- Order Detail Modal -->
<div id="orderDetailModal" class="modal">
    <div class="modal-content modal-xl">
        <div class="modal-header">
            <h3><i class="fas fa-shopping-bag"></i> Chi tiết đơn hàng <span id="modalOrderNumber"></span></h3>
            <button class="modal-close" onclick="closeModal('orderDetailModal')">&times;</button>
        </div>
        <div class="modal-body" id="orderDetailContent">
            <!-- Content will be loaded dynamically -->
            <div class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i> Đang tải...
            </div>
        </div>
    </div>
</div>

<!-- Update Status Modal -->
<div id="updateStatusModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-edit"></i> Cập nhật trạng thái đơn hàng</h3>
            <button class="modal-close" onclick="closeModal('updateStatusModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="updateStatusForm" action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="update_order_status">
                <input type="hidden" name="orderId" id="updateOrderId">

                <div class="form-group">
                    <label>Đơn hàng:</label>
                    <p><strong id="updateOrderNumber"></strong></p>
                </div>

                <div class="form-group">
                    <label for="newStatus">Trạng thái mới: <span class="required">*</span></label>
                    <select name="newStatus" id="newStatus" class="form-control" required>
                        <option value="PENDING">Chờ duyệt</option>
                        <option value="CONFIRMED">Đã xác nhận</option>
                        <option value="SHIPPING">Đang giao hàng</option>
                        <option value="COMPLETED">Hoàn thành</option>
                        <option value="CANCELLED">Hủy đơn</option>
                    </select>
                </div>

                <div class="form-group" id="cancelReasonGroup" style="display: none;">
                    <label for="cancelReason">Lý do hủy: <span class="required">*</span></label>
                    <textarea name="cancelReason" id="cancelReason" class="form-control" rows="3" placeholder="Nhập lý do hủy đơn hàng..."></textarea>
                </div>

                <div class="form-group">
                    <label for="statusNote">Ghi chú:</label>
                    <textarea name="statusNote" id="statusNote" class="form-control" rows="2" placeholder="Ghi chú cho việc cập nhật trạng thái..."></textarea>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal('updateStatusModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Cập nhật
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Print Invoice Template (Hidden) -->
<div id="invoiceTemplate" style="display: none;">
    <!-- Invoice content will be generated dynamically -->
</div>

<style>
    /* Order Management Styles */
    .filter-section {
        background: var(--bg-card);
        padding: 16px;
        border-radius: 8px;
        border: 1px solid var(--border-color);
    }

    .filter-group {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .filter-group label {
        font-size: 13px;
        color: var(--text-secondary);
        white-space: nowrap;
    }

    .filter-group select,
    .filter-group input {
        padding: 8px 12px;
        border: 1px solid var(--border-color);
        border-radius: 6px;
        font-size: 13px;
    }

    .stats-grid.mini {
        gap: 12px;
    }

    .stat-card.mini {
        padding: 12px 16px;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .stat-card.mini .stat-icon {
        width: 40px;
        height: 40px;
        font-size: 16px;
    }

    .stat-card.mini .stat-info {
        display: flex;
        flex-direction: column;
    }

    .stat-card.mini .stat-number {
        font-size: 20px;
        font-weight: 700;
    }

    .stat-card.mini .stat-label {
        font-size: 12px;
        color: var(--text-muted);
    }

    .stat-icon.purple { background: linear-gradient(135deg, #9333EA, #7C3AED); }
    .stat-icon.red { background: linear-gradient(135deg, #EF4444, #DC2626); }

    .customer-info {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .customer-avatar {
        width: 32px;
        height: 32px;
        border-radius: 50%;
    }

    .order-number {
        color: var(--primary-color);
        font-family: 'JetBrains Mono', monospace;
    }

    /* Status Badges */
    .status-badge {
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 12px;
        font-weight: 500;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }

    .status-pending { background: #FEF3C7; color: #D97706; }
    .status-confirmed { background: #DBEAFE; color: #2563EB; }
    .status-shipping { background: #E9D5FF; color: #7C3AED; }
    .status-completed { background: #D1FAE5; color: #059669; }
    .status-cancelled { background: #FEE2E2; color: #DC2626; }

    .badge-success { background: #D1FAE5; color: #059669; }
    .badge-warning { background: #FEF3C7; color: #D97706; }

    .btn-icon.print {
        background: linear-gradient(135deg, #6366F1, #4F46E5);
        color: white;
    }

    .btn-icon.print:hover {
        transform: scale(1.1);
    }

    /* Modal XL */
    .modal-xl {
        max-width: 900px;
    }

    .loading-spinner {
        text-align: center;
        padding: 40px;
        color: var(--text-muted);
    }

    .loading-spinner i {
        font-size: 32px;
        margin-bottom: 12px;
        display: block;
    }

    /* Pagination */
    .pagination {
        display: flex;
        justify-content: center;
        gap: 8px;
        margin-top: 24px;
    }

    .page-link {
        padding: 8px 14px;
        border: 1px solid var(--border-color);
        border-radius: 6px;
        color: var(--text-primary);
        text-decoration: none;
        transition: all 0.2s;
    }

    .page-link:hover,
    .page-link.active {
        background: var(--primary-color);
        color: white;
        border-color: var(--primary-color);
    }

    /* Order Detail Styles */
    .order-detail-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 24px;
    }

    .order-detail-section {
        background: var(--bg-primary);
        padding: 16px;
        border-radius: 8px;
        border: 1px solid var(--border-color);
    }

    .order-detail-section h4 {
        margin: 0 0 12px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid var(--border-color);
        color: var(--text-primary);
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .order-detail-section h4 i {
        color: var(--primary-color);
    }

    .detail-row {
        display: flex;
        justify-content: space-between;
        padding: 8px 0;
        border-bottom: 1px dashed var(--border-color);
    }

    .detail-row:last-child {
        border-bottom: none;
    }

    .detail-label {
        color: var(--text-muted);
        font-size: 13px;
    }

    .detail-value {
        font-weight: 500;
        color: var(--text-primary);
    }

    .order-items-table {
        width: 100%;
        margin-top: 16px;
    }

    .order-items-table th,
    .order-items-table td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid var(--border-color);
    }

    .order-items-table th {
        background: var(--bg-primary);
        font-weight: 600;
        font-size: 13px;
        color: var(--text-secondary);
    }

    .order-total-row {
        font-weight: 700;
        font-size: 16px;
        color: var(--primary-color);
    }
</style>

<script>
    // View Order Detail
    function viewOrderDetail(orderId) {
        document.getElementById('orderDetailContent').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
        openModal('orderDetailModal');

        fetch('${pageContext.request.contextPath}/admin?action=get_order_detail&orderId=' + orderId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('modalOrderNumber').textContent = '#' + data.order.orderNumber;
                    document.getElementById('orderDetailContent').innerHTML = generateOrderDetailHTML(data);
                } else {
                    document.getElementById('orderDetailContent').innerHTML = '<p class="error">Không thể tải thông tin đơn hàng</p>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('orderDetailContent').innerHTML = '<p class="error">Đã có lỗi xảy ra</p>';
            });
    }

    function generateOrderDetailHTML(data) {
        const order = data.order;
        const items = data.items || [];
        const address = data.address;

        let itemsHTML = '';
        items.forEach(item => {
            itemsHTML += `
                <tr>
                    <td>${item.productName}</td>
                    <td>${item.variantInfo || '-'}</td>
                    <td>${item.quantity}</td>
                    <td>${formatCurrency(item.unitPrice)}</td>
                    <td>${formatCurrency(item.subtotal)}</td>
                </tr>
            `;
        });

        return `
            <div class="order-detail-grid">
                <div class="order-detail-section">
                    <h4><i class="fas fa-info-circle"></i> Thông tin đơn hàng</h4>
                    <div class="detail-row">
                        <span class="detail-label">Mã đơn:</span>
                        <span class="detail-value">#${order.orderNumber}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Ngày đặt:</span>
                        <span class="detail-value">${order.orderDate}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Trạng thái:</span>
                        <span class="status-badge status-${order.status.toLowerCase()}">${getStatusText(order.status)}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Ghi chú:</span>
                        <span class="detail-value">${order.notes || 'Không có'}</span>
                    </div>
                </div>

                <div class="order-detail-section">
                    <h4><i class="fas fa-map-marker-alt"></i> Địa chỉ giao hàng</h4>
                    <div class="detail-row">
                        <span class="detail-label">Người nhận:</span>
                        <span class="detail-value">${address ? address.receiverName : '-'}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Điện thoại:</span>
                        <span class="detail-value">${address ? address.phone : '-'}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Địa chỉ:</span>
                        <span class="detail-value">${address ? (address.street + ', ' + address.ward + ', ' + address.district + ', ' + address.city) : '-'}</span>
                    </div>
                </div>
            </div>

            <div class="order-detail-section" style="margin-top: 24px;">
                <h4><i class="fas fa-box"></i> Sản phẩm trong đơn hàng</h4>
                <table class="order-items-table">
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Phiên bản</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${itemsHTML}
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="4" style="text-align: right;">Tạm tính:</td>
                            <td>${formatCurrency(order.subtotal)}</td>
                        </tr>
                        <tr>
                            <td colspan="4" style="text-align: right;">Phí vận chuyển:</td>
                            <td>${formatCurrency(order.shippingFee)}</td>
                        </tr>
                        <tr>
                            <td colspan="4" style="text-align: right;">Giảm giá:</td>
                            <td>-${formatCurrency(order.discount + order.voucherDiscount)}</td>
                        </tr>
                        <tr class="order-total-row">
                            <td colspan="4" style="text-align: right;">Tổng cộng:</td>
                            <td>${formatCurrency(order.totalPrice)}</td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div style="margin-top: 20px; display: flex; gap: 12px; justify-content: flex-end;">
                <button class="btn btn-secondary" onclick="printInvoice(${order.orderId})">
                    <i class="fas fa-print"></i> In hóa đơn
                </button>
                <button class="btn btn-primary" onclick="closeModal('orderDetailModal'); openUpdateStatusModal(${order.orderId}, '${order.status}', '${order.orderNumber}')">
                    <i class="fas fa-edit"></i> Cập nhật trạng thái
                </button>
            </div>
        `;
    }

    function getStatusText(status) {
        const statusMap = {
            'PENDING': 'Chờ duyệt',
            'CONFIRMED': 'Đã xác nhận',
            'SHIPPING': 'Đang giao',
            'COMPLETED': 'Hoàn thành',
            'CANCELLED': 'Đã hủy'
        };
        return statusMap[status] || status;
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    }

    // Open Update Status Modal
    function openUpdateStatusModal(orderId, currentStatus, orderNumber) {
        document.getElementById('updateOrderId').value = orderId;
        document.getElementById('updateOrderNumber').textContent = '#' + orderNumber;
        document.getElementById('newStatus').value = currentStatus;
        document.getElementById('cancelReason').value = '';
        document.getElementById('statusNote').value = '';

        toggleCancelReason();
        openModal('updateStatusModal');
    }

    // Toggle Cancel Reason Field
    document.getElementById('newStatus').addEventListener('change', toggleCancelReason);

    function toggleCancelReason() {
        const status = document.getElementById('newStatus').value;
        const cancelGroup = document.getElementById('cancelReasonGroup');
        const cancelInput = document.getElementById('cancelReason');

        if (status === 'CANCELLED') {
            cancelGroup.style.display = 'block';
            cancelInput.setAttribute('required', 'required');
        } else {
            cancelGroup.style.display = 'none';
            cancelInput.removeAttribute('required');
        }
    }

    // Print Invoice
    function printInvoice(orderId) {
        window.open('${pageContext.request.contextPath}/admin?action=print_invoice&orderId=' + orderId, '_blank', 'width=800,height=600');
    }

    // Export Orders
    function exportOrders() {
        const params = new URLSearchParams(window.location.search);
        params.set('action', 'export_orders');
        window.location.href = '${pageContext.request.contextPath}/admin?' + params.toString();
    }

    // Toggle Select All
    function toggleSelectAllOrders() {
        const selectAll = document.getElementById('selectAllOrders');
        const checkboxes = document.querySelectorAll('.order-checkbox');
        checkboxes.forEach(cb => cb.checked = selectAll.checked);
    }
</script>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hóa đơn #${order.orderNumber} - VietTech</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-size: 14px;
            line-height: 1.5;
            color: #333;
            background: #fff;
        }

        .invoice-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 40px;
        }

        /* Header */
        .invoice-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            padding-bottom: 30px;
            border-bottom: 2px solid #2563EB;
            margin-bottom: 30px;
        }

        .company-info h1 {
            font-size: 28px;
            color: #2563EB;
            margin-bottom: 8px;
        }

        .company-info h1 span {
            color: #333;
        }

        .company-info p {
            color: #666;
            font-size: 13px;
        }

        .invoice-title {
            text-align: right;
        }

        .invoice-title h2 {
            font-size: 32px;
            color: #333;
            margin-bottom: 8px;
        }

        .invoice-number {
            font-size: 16px;
            color: #2563EB;
            font-weight: 600;
        }

        .invoice-date {
            color: #666;
            margin-top: 4px;
        }

        /* Info Sections */
        .info-section {
            display: flex;
            gap: 40px;
            margin-bottom: 30px;
        }

        .info-box {
            flex: 1;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }

        .info-box h3 {
            font-size: 14px;
            color: #666;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 12px;
            padding-bottom: 8px;
            border-bottom: 1px solid #ddd;
        }

        .info-box p {
            margin-bottom: 4px;
        }

        .info-box strong {
            color: #333;
        }

        /* Items Table */
        .items-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        .items-table th {
            background: #2563EB;
            color: white;
            padding: 12px 16px;
            text-align: left;
            font-weight: 500;
        }

        .items-table th:first-child {
            border-radius: 8px 0 0 0;
        }

        .items-table th:last-child {
            border-radius: 0 8px 0 0;
            text-align: right;
        }

        .items-table td {
            padding: 12px 16px;
            border-bottom: 1px solid #eee;
        }

        .items-table td:last-child {
            text-align: right;
        }

        .items-table tbody tr:hover {
            background: #f8f9fa;
        }

        .product-name {
            font-weight: 500;
        }

        .variant-info {
            font-size: 12px;
            color: #666;
        }

        /* Summary */
        .summary-section {
            display: flex;
            justify-content: flex-end;
        }

        .summary-box {
            width: 300px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px dashed #ddd;
        }

        .summary-row:last-child {
            border-bottom: none;
        }

        .summary-label {
            color: #666;
        }

        .summary-value {
            font-weight: 500;
        }

        .total-row {
            background: #f8f9fa;
            padding: 12px;
            border-radius: 8px;
            margin-top: 8px;
        }

        .total-row .summary-label,
        .total-row .summary-value {
            font-size: 18px;
            font-weight: 700;
            color: #2563EB;
        }

        /* Status Badge */
        .status-badge {
            display: inline-block;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 500;
        }

        .status-pending { background: #FEF3C7; color: #D97706; }
        .status-confirmed { background: #DBEAFE; color: #2563EB; }
        .status-shipping { background: #E9D5FF; color: #7C3AED; }
        .status-completed { background: #D1FAE5; color: #059669; }
        .status-cancelled { background: #FEE2E2; color: #DC2626; }

        /* Footer */
        .invoice-footer {
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            text-align: center;
            color: #666;
            font-size: 13px;
        }

        .invoice-footer p {
            margin-bottom: 4px;
        }

        /* Print Styles */
        @media print {
            body {
                print-color-adjust: exact;
                -webkit-print-color-adjust: exact;
            }

            .invoice-container {
                padding: 20px;
            }

            .no-print {
                display: none !important;
            }
        }

        /* Print Button */
        .print-actions {
            position: fixed;
            top: 20px;
            right: 20px;
            display: flex;
            gap: 12px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.2s;
        }

        .btn-primary {
            background: #2563EB;
            color: white;
        }

        .btn-primary:hover {
            background: #1d4ed8;
        }

        .btn-secondary {
            background: #6B7280;
            color: white;
        }

        .btn-secondary:hover {
            background: #4B5563;
        }
    </style>
</head>
<body>
    <div class="print-actions no-print">
        <button class="btn btn-primary" onclick="window.print()">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M6 9V2h12v7"></path>
                <path d="M6 18H4a2 2 0 01-2-2v-5a2 2 0 012-2h16a2 2 0 012 2v5a2 2 0 01-2 2h-2"></path>
                <rect x="6" y="14" width="12" height="8"></rect>
            </svg>
            In hóa đơn
        </button>
        <button class="btn btn-secondary" onclick="window.close()">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
            Đóng
        </button>
    </div>

    <div class="invoice-container">
        <!-- Header -->
        <div class="invoice-header">
            <div class="company-info">
                <h1>Viet<span>Tech</span></h1>
                <p>123 Nguyễn Văn Linh, Quận 7, TP.HCM</p>
                <p>Hotline: 1900 1234 | Email: contact@viettech.com</p>
            </div>
            <div class="invoice-title">
                <h2>HÓA ĐƠN</h2>
                <div class="invoice-number">#${order.orderNumber}</div>
                <div class="invoice-date">
                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
                <div style="margin-top: 8px;">
                    <span class="status-badge status-${order.status.toLowerCase()}">
                        <c:choose>
                            <c:when test="${order.status == 'PENDING'}">Chờ duyệt</c:when>
                            <c:when test="${order.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                            <c:when test="${order.status == 'SHIPPING'}">Đang giao</c:when>
                            <c:when test="${order.status == 'COMPLETED'}">Hoàn thành</c:when>
                            <c:when test="${order.status == 'CANCELLED'}">Đã hủy</c:when>
                            <c:otherwise>${order.status}</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>

        <!-- Customer & Shipping Info -->
        <div class="info-section">
            <div class="info-box">
                <h3>Thông tin khách hàng</h3>
                <c:if test="${customer != null}">
                    <p><strong>${customer.firstName} ${customer.lastName}</strong></p>
                    <p>Email: ${customer.email}</p>
                    <p>SĐT: ${customer.phone}</p>
                </c:if>
                <c:if test="${customer == null}">
                    <p>Customer ID: ${order.customerId}</p>
                </c:if>
            </div>
            <div class="info-box">
                <h3>Địa chỉ giao hàng</h3>
                <c:if test="${address != null}">
                    <p><strong>${address.receiverName}</strong></p>
                    <p>SĐT: ${address.phone}</p>
                    <p>${address.street}, ${address.ward}</p>
                    <p>${address.district}, ${address.city}</p>
                </c:if>
                <c:if test="${address == null}">
                    <p>Address ID: ${order.addressId}</p>
                </c:if>
            </div>
        </div>

        <!-- Order Items -->
        <table class="items-table">
            <thead>
                <tr>
                    <th style="width: 50%;">Sản phẩm</th>
                    <th style="width: 15%;">Đơn giá</th>
                    <th style="width: 10%;">SL</th>
                    <th style="width: 25%;">Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${orderDetails}">
                    <tr>
                        <td>
                            <div class="product-name">${item.productName}</div>
                            <c:if test="${not empty item.variantInfo}">
                                <div class="variant-info">${item.variantInfo}</div>
                            </c:if>
                        </td>
                        <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Summary -->
        <div class="summary-section">
            <div class="summary-box">
                <div class="summary-row">
                    <span class="summary-label">Tạm tính:</span>
                    <span class="summary-value"><fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                </div>
                <div class="summary-row">
                    <span class="summary-label">Phí vận chuyển:</span>
                    <span class="summary-value"><fmt:formatNumber value="${order.shippingFee}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                </div>
                <c:if test="${order.discount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Giảm giá:</span>
                        <span class="summary-value" style="color: #059669;">-<fmt:formatNumber value="${order.discount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                    </div>
                </c:if>
                <c:if test="${order.voucherDiscount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Voucher:</span>
                        <span class="summary-value" style="color: #059669;">-<fmt:formatNumber value="${order.voucherDiscount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                    </div>
                </c:if>
                <c:if test="${order.loyaltyPointsDiscount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Điểm tích lũy:</span>
                        <span class="summary-value" style="color: #059669;">-<fmt:formatNumber value="${order.loyaltyPointsDiscount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                    </div>
                </c:if>
                <div class="summary-row total-row">
                    <span class="summary-label">TỔNG CỘNG:</span>
                    <span class="summary-value"><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                </div>
            </div>
        </div>

        <!-- Notes -->
        <c:if test="${not empty order.notes}">
            <div style="margin-top: 30px; padding: 16px; background: #fff8e1; border-radius: 8px; border-left: 4px solid #FFA000;">
                <strong style="color: #F57C00;">📝 Ghi chú:</strong>
                <p style="margin-top: 8px;">${order.notes}</p>
            </div>
        </c:if>

        <!-- Footer -->
        <div class="invoice-footer">
            <p><strong>Cảm ơn quý khách đã mua hàng tại VietTech!</strong></p>
            <p>Mọi thắc mắc vui lòng liên hệ: 1900 1234 hoặc support@viettech.com</p>
            <p style="margin-top: 12px; font-size: 11px; color: #999;">
                Hóa đơn được tạo tự động bởi hệ thống VietTech -
                <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/>
            </p>
        </div>
    </div>

    <script>
        // Auto print on load (optional)
        // window.onload = function() { window.print(); }
    </script>
</body>
</html>


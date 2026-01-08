<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thanh toán thành công | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order-confirmation.css">
</head>
<body>
<jsp:include page="../../header.jsp" />

<div class="confirmation-container">
  <div class="container">
    <div class="confirmation-card">
      <div class="header">
        <div class="success-icon">
          <i class="bi bi-check-circle"></i>
        </div>
        <h1>Thanh toán thành công!</h1>
        <p class="order-number">Mã đơn hàng: <strong>${order.orderNumber}</strong></p>
        <p class="text-muted">Đơn hàng của bạn đã được xác nhận và đang được xử lý</p>
      </div>

      <div class="content">
        <!-- Thông tin giao dịch VNPay -->
        <div class="section">
          <h2 class="section-title">
            <i class="bi bi-credit-card-2-front me-2"></i>Thông tin giao dịch
          </h2>
          <div class="info-row">
            <span class="info-label">Mã giao dịch VNPay:</span>
            <span class="text-primary fw-semibold">${sessionScope.transactionNo}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Số tiền thanh toán:</span>
            <span class="text-danger fw-bold">
                            <fmt:formatNumber value="${sessionScope.amount}" pattern="#,###"/>đ
                        </span>
          </div>
          <c:if test="${not empty sessionScope.bankCode}">
            <div class="info-row">
              <span class="info-label">Ngân hàng:</span>
              <span class="text-uppercase">${sessionScope.bankCode}</span>
            </div>
          </c:if>
          <div class="info-row">
            <span class="info-label">Thời gian thanh toán:</span>
            <span>${sessionScope.payDate}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Trạng thái:</span>
            <span class="badge bg-success">Đã thanh toán</span>
          </div>
        </div>

        <!-- Thông tin khách hàng -->
        <div class="section">
          <h2 class="section-title">
            <i class="bi bi-person-circle me-2"></i>Thông tin khách hàng
          </h2>
          <div class="info-row">
            <span class="info-label">Họ tên:</span>
            <span>${customer.firstName} ${customer.lastName}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Tên đăng nhập:</span>
            <span>${customer.username}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Email:</span>
            <span>${customer.email}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Số điện thoại:</span>
            <span>${customer.phone}</span>
          </div>
          <div class="info-row">
            <span class="info-label">Hạng thành viên:</span>
            <span class="badge bg-warning text-dark">
              ${customer.membershipTier}
            </span>
          </div>
          <div class="info-row">
            <span class="info-label">Điểm tích lũy:</span>
            <span>${customer.loyaltyPoints} điểm</span>
          </div>
        </div>

        <!-- Địa chỉ giao hàng -->
        <div class="section">
          <h2 class="section-title">
            <i class="bi bi-geo-alt me-2"></i>Địa chỉ giao hàng
          </h2>
          <div class="address-content">
            <p class="mb-1">${shippingAddress.street}</p>
            <p class="mb-1">${shippingAddress.ward}, ${shippingAddress.district}</p>
            <p class="mb-0">${shippingAddress.city}</p>
          </div>
        </div>

        <!-- Sản phẩm đã đặt -->
        <div class="section">
          <h2 class="section-title">
            <i class="bi bi-box-seam me-2"></i>Sản phẩm đã đặt
          </h2>
          <div class="item-list">
            <c:forEach var="item" items="${cartItems}">
              <div class="item d-flex justify-content-between align-items-center">
                <div class="item-info d-flex align-items-center gap-3">
                  <img src="${item.productImage}"
                       alt="${item.productName}"
                       style="width:70px;height:70px;object-fit:cover;border-radius:8px">

                  <div>
                    <div class="item-name fw-semibold">
                        ${item.productName}
                    </div>

                    <c:if test="${not empty item.variantDisplay}">
                      <div class="text-muted small">
                        Phân loại: ${item.variantDisplay}
                      </div>
                    </c:if>

                    <div class="item-quantity">
                      SL: ${item.quantity} ×
                      <fmt:formatNumber value="${item.price}" pattern="#,###"/>đ
                    </div>
                  </div>
                </div>

                <div class="item-price fw-bold text-danger">
                  <fmt:formatNumber value="${item.subtotal}" pattern="#,###"/>đ
                </div>
              </div>
            </c:forEach>
          </div>
        </div>

        <!-- Chi tiết thanh toán -->
        <div class="section">
          <h2 class="section-title">
            <i class="bi bi-receipt me-2"></i>Chi tiết thanh toán
          </h2>
          <div class="summary">
            <div class="summary-row">
              <span>Tạm tính:</span>
              <span><fmt:formatNumber value="${subtotal}" type="number" pattern="#,###"/>đ</span>
            </div>
            <div class="summary-row">
              <span>Phí vận chuyển:</span>
              <span><fmt:formatNumber value="${shippingFee}" type="number" pattern="#,###"/>đ</span>
            </div>
            <c:if test="${voucherDiscount > 0}">
              <div class="summary-row discount-row">
                <span>Giảm giá voucher:</span>
                <span>-<fmt:formatNumber value="${voucherDiscount}" type="number" pattern="#,###"/>đ</span>
              </div>
            </c:if>
            <div class="summary-row total">
              <span>Tổng cộng:</span>
              <span><fmt:formatNumber value="${totalPrice}" type="number" pattern="#,###"/>đ</span>
            </div>
          </div>

          <div class="payment-details mt-3">
            <div class="info-row">
                            <span class="info-label">
                                <i class="bi bi-credit-card me-2"></i>Phương thức thanh toán:
                            </span>
              <span><strong>VNPay (Thanh toán online)</strong></span>
            </div>

            <div class="info-row">
                            <span class="info-label">
                                <i class="bi bi-calendar-check me-2"></i>Ngày đặt hàng:
                            </span>
              <span><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></span>
            </div>

            <div class="info-row">
                            <span class="info-label">
                                <i class="bi bi-truck me-2"></i>Dự kiến giao hàng:
                            </span>
              <span><fmt:formatDate value="${order.estimatedDelivery}" pattern="dd/MM/yyyy"/></span>
            </div>
          </div>
        </div>

        <!-- Thông báo -->
        <div class="alert alert-success" role="alert">
          <i class="bi bi-check-circle me-2"></i>
          Thanh toán đã được xác nhận thành công. Chúng tôi đã gửi email xác nhận đơn hàng đến địa chỉ của bạn.
        </div>

        <!-- Buttons -->
        <div class="btn-group-custom">
          <a href="${pageContext.request.contextPath}/profile/orders" class="btn btn-primary btn-lg">
            <i class="bi bi-list-ul me-2"></i>Xem đơn hàng
          </a>
          <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary btn-lg">
            <i class="bi bi-house me-2"></i>Tiếp tục mua sắm
          </a>
        </div>

        <div class="mt-4 text-center">
          <small class="text-muted">
            Cần hỗ trợ? Liên hệ
            <a href="${pageContext.request.contextPath}/contact">Chăm sóc khách hàng</a>
          </small>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
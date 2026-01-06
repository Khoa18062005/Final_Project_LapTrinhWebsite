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

  <style>
    .success-container {
      min-height: 80vh;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .success-card {
      max-width: 600px;
      width: 100%;
      text-align: center;
      padding: 3rem;
      border-radius: 15px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    }

    .success-icon {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 2rem;
      animation: scaleIn 0.5s ease-out;
    }

    .success-icon i {
      font-size: 3rem;
      color: white;
    }

    @keyframes scaleIn {
      from {
        transform: scale(0);
        opacity: 0;
      }
      to {
        transform: scale(1);
        opacity: 1;
      }
    }

    .order-info {
      background: #f8f9fa;
      border-radius: 10px;
      padding: 1.5rem;
      margin: 2rem 0;
    }

    .order-info-row {
      display: flex;
      justify-content: space-between;
      padding: 0.75rem 0;
      border-bottom: 1px solid #dee2e6;
    }

    .order-info-row:last-child {
      border-bottom: none;
    }

    .order-info-label {
      font-weight: 600;
      color: #6c757d;
    }

    .order-info-value {
      font-weight: 500;
      color: #212529;
    }

    .btn-group-custom {
      display: flex;
      gap: 1rem;
      justify-content: center;
      margin-top: 2rem;
    }
  </style>
</head>
<body>
<jsp:include page="../../header.jsp" />

<div class="success-container">
  <div class="container">
    <div class="success-card">
      <div class="success-icon">
        <i class="bi bi-check-circle"></i>
      </div>

      <h2 class="mb-3">Thanh toán thành công!</h2>
      <p class="text-muted mb-4">
        Đơn hàng của bạn đã được xác nhận và đang được xử lý.
      </p>

      <div class="order-info">
        <div class="order-info-row">
          <span class="order-info-label">Mã đơn hàng:</span>
          <span class="order-info-value">${sessionScope.orderNumber}</span>
        </div>

        <div class="order-info-row">
          <span class="order-info-label">Mã giao dịch:</span>
          <span class="order-info-value">${sessionScope.transactionNo}</span>
        </div>

        <div class="order-info-row">
          <span class="order-info-label">Số tiền thanh toán:</span>
          <span class="order-info-value">
                            <fmt:formatNumber value="${sessionScope.amount}" type="currency"
                                              currencySymbol="₫" groupingUsed="true" />
                        </span>
        </div>

        <c:if test="${not empty sessionScope.bankCode}">
          <div class="order-info-row">
            <span class="order-info-label">Ngân hàng:</span>
            <span class="order-info-value">${sessionScope.bankCode}</span>
          </div>
        </c:if>

        <div class="order-info-row">
          <span class="order-info-label">Thời gian:</span>
          <span class="order-info-value">${sessionScope.payDate}</span>
        </div>
      </div>

      <div class="alert alert-info" role="alert">
        <i class="bi bi-info-circle me-2"></i>
        Chúng tôi đã gửi email xác nhận đơn hàng đến địa chỉ của bạn.
      </div>

      <div class="btn-group-custom">
        <a href="${pageContext.request.contextPath}/profile/orders" class="btn btn-primary btn-lg">
          <i class="bi bi-list-ul me-2"></i>Xem đơn hàng
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary btn-lg">
          <i class="bi bi-house me-2"></i>Về trang chủ
        </a>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
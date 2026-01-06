<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thanh toán thất bại | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">

  <style>
    .failed-container {
      min-height: 80vh;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .failed-card {
      max-width: 600px;
      width: 100%;
      text-align: center;
      padding: 3rem;
      border-radius: 15px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    }

    .failed-icon {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 2rem;
      animation: shake 0.5s ease-out;
    }

    .failed-icon i {
      font-size: 3rem;
      color: white;
    }

    @keyframes shake {
      0%, 100% { transform: translateX(0); }
      10%, 30%, 50%, 70%, 90% { transform: translateX(-10px); }
      20%, 40%, 60%, 80% { transform: translateX(10px); }
    }

    .error-message {
      background: #fff3cd;
      border-left: 4px solid #ffc107;
      padding: 1rem;
      border-radius: 5px;
      margin: 2rem 0;
      text-align: left;
    }

    .btn-group-custom {
      display: flex;
      gap: 1rem;
      justify-content: center;
      margin-top: 2rem;
    }

    .help-text {
      margin-top: 2rem;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 10px;
    }

    .help-text ul {
      text-align: left;
      margin-bottom: 0;
    }
  </style>
</head>
<body>
<jsp:include page="../../header.jsp" />

<div class="failed-container">
  <div class="container">
    <div class="failed-card">
      <div class="failed-icon">
        <i class="bi bi-x-circle"></i>
      </div>

      <h2 class="mb-3">Thanh toán thất bại!</h2>
      <p class="text-muted mb-4">
        Rất tiếc, giao dịch của bạn không thành công.
      </p>

      <c:if test="${not empty sessionScope.paymentError}">
        <div class="error-message">
          <strong><i class="bi bi-exclamation-triangle me-2"></i>Lỗi:</strong>
            ${sessionScope.paymentError}
        </div>
      </c:if>

      <div class="help-text">
        <h6 class="mb-3"><i class="bi bi-lightbulb me-2"></i>Có thể do các nguyên nhân sau:</h6>
        <ul>
          <li>Tài khoản không đủ số dư</li>
          <li>Thông tin thẻ không chính xác</li>
          <li>Đã hết thời gian thanh toán</li>
          <li>Thẻ/tài khoản chưa đăng ký dịch vụ thanh toán online</li>
          <li>Ngân hàng đang bảo trì hệ thống</li>
        </ul>
      </div>

      <div class="alert alert-info mt-4" role="alert">
        <i class="bi bi-info-circle me-2"></i>
        Vui lòng kiểm tra lại thông tin và thử lại sau ít phút.
      </div>

      <div class="btn-group-custom">
        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg">
          <i class="bi bi-arrow-clockwise me-2"></i>Thử lại
        </a>
        <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary btn-lg">
          <i class="bi bi-cart me-2"></i>Giỏ hàng
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary btn-lg">
          <i class="bi bi-house me-2"></i>Trang chủ
        </a>
      </div>

      <div class="mt-4">
        <small class="text-muted">
          Cần hỗ trợ? Liên hệ
          <a href="${pageContext.request.contextPath}/contact">Chăm sóc khách hàng</a>
        </small>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../../footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
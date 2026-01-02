<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Xác Thực OTP | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">

  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/verify-otp-forgot.css">

</head>
<body>

<div class="card">
  <div class="card-header">
    <h3>Xác Thực OTP</h3>
    <p class="mb-0">Nhập mã xác thực từ email của bạn</p>
  </div>
  <div class="card-body">

    <!-- THÔNG BÁO EMAIL -->
    <div class="email-info">
      <c:choose>
        <c:when test="${not empty maskedEmail}">
          <strong>Mã OTP đã được gửi đến:</strong><br>
          <span class="text-primary fw-bold">${maskedEmail}</span>
        </c:when>
        <c:otherwise>
          <strong>Vui lòng nhập mã OTP đã gửi đến email của bạn</strong>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- THÔNG BÁO LỖI -->
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Lỗi!</strong> ${errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <!-- THÔNG BÁO THÀNH CÔNG -->
    <c:if test="${not empty infoMessage}">
      <div class="alert alert-info alert-dismissible fade show" role="alert">
        <strong>Thông báo!</strong> ${infoMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-forgot-otp" method="post" id="otpForm">

      <!-- OTP Input -->
      <div class="mb-4">
        <label for="otp" class="form-label">Mã OTP (6 chữ số)</label>
        <input type="text"
               class="form-control otp-input"
               id="otp"
               name="otp"
               maxlength="6"
               pattern="[0-9]{6}"
               placeholder="Nhập 6 chữ số"
               required
               autofocus
               <c:if test="${isExpired}">disabled</c:if>>
        <div class="mt-2">
          <span id="timer" class="timer"></span>
          <span id="timerText" class="ms-2"></span>
        </div>
        <small class="text-muted">Nhập đúng 6 chữ số từ email của bạn</small>
      </div>

      <!-- Nút xác thực -->
      <button type="submit"
              class="btn btn-primary w-100 mb-3"
              id="verifyBtn"
              <c:if test="${isExpired}">disabled</c:if>>
        Xác Thực OTP
      </button>

      <!-- Nút gửi lại OTP -->
      <button type="button"
              class="btn btn-outline-primary w-100"
              id="resendBtn"
              <c:if test="${not isExpired}">disabled</c:if>>
                <span id="resendText">
                    <c:choose>
                      <c:when test="${isExpired}">Gửi lại OTP</c:when>
                      <c:otherwise>Gửi lại OTP (90s)</c:otherwise>
                    </c:choose>
                </span>
      </button>
    </form>

    <!-- Các liên kết phụ -->
    <div class="text-center mt-4">
      <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none">
        Nhập email khác
      </a>
    </div>
    <div class="text-center mt-2">
      <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
        Quay lại đăng nhập
      </a>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Hidden input để lấy thời gian từ server -->
<input type="hidden" id="serverTimeLeft" value="${timeLeft}">

<script src="${pageContext.request.contextPath}/assets/js/verify-otp-forgot.js"></script>


</body>
</html>
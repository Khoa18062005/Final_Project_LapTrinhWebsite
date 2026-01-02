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

  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px;
    }
    .card {
      border: none;
      border-radius: 20px;
      overflow: hidden;
      box-shadow: 0 15px 35px rgba(0,0,0,0.2);
      max-width: 500px;
      width: 100%;
    }
    .card-header {
      background: linear-gradient(135deg, #0d6efd, #0b5ed7);
      color: white;
      text-align: center;
      padding: 2rem;
    }
    .card-body {
      padding: 2.5rem;
    }
    .form-control {
      border-radius: 10px;
      padding: 0.8rem 1rem;
      border: 1px solid #ced4da;
    }
    .btn-primary {
      border-radius: 10px;
      padding: 0.8rem;
      font-weight: 600;
      background: linear-gradient(135deg, #0d6efd, #0b5ed7);
      border: none;
    }
    .btn-primary:hover {
      background: linear-gradient(135deg, #0b5ed7, #094ab2);
    }
    .btn-primary:disabled {
      background: #6c757d;
      cursor: not-allowed;
    }
    .email-info {
      background: #f8f9fa;
      border-radius: 10px;
      padding: 15px;
      text-align: center;
      margin-bottom: 20px;
      border-left: 4px solid #0d6efd;
    }
    .otp-input {
      font-size: 1.5rem;
      text-align: center;
      letter-spacing: 5px;
      font-weight: bold;
      padding: 1rem;
    }
    .timer {
      font-size: 1.2rem;
      font-weight: bold;
    }
    .text-success { color: #198754; }
    .text-warning { color: #ffc107; }
    .text-danger { color: #dc3545; }
    .card-header h3 {
      font-weight: 700;
      margin-bottom: 0.5rem;
    }
    .card-header p {
      opacity: 0.9;
      font-size: 0.95rem;
    }
  </style>
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

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const otpInput = document.getElementById('otp');
    const verifyBtn = document.getElementById('verifyBtn');
    const resendBtn = document.getElementById('resendBtn');
    const resendText = document.getElementById('resendText');
    const timerElement = document.getElementById('timer');
    const timerText = document.getElementById('timerText');

    // Lấy thời gian còn lại từ server
    let timeLeft = parseInt(document.getElementById('serverTimeLeft').value) || 90;
    let timerInterval = null;

    // Format OTP input
    otpInput.addEventListener('input', function() {
      this.value = this.value.replace(/\D/g, '').slice(0, 6);
    });

    // Hàm cập nhật hiển thị timer
    function updateTimerDisplay() {
      timerElement.textContent = timeLeft;

      if (timeLeft > 30) {
        timerElement.className = 'timer text-success';
        timerText.textContent = 'giây còn lại';
      } else if (timeLeft > 10) {
        timerElement.className = 'timer text-warning';
        timerText.textContent = 'giây còn lại';
      } else if (timeLeft > 0) {
        timerElement.className = 'timer text-danger';
        timerText.textContent = 'giây còn lại';
      } else {
        timerElement.textContent = '0';
        timerElement.className = 'timer text-danger';
        timerText.textContent = 'Mã OTP đã hết hạn';

        // Disable verify button, enable resend button
        verifyBtn.disabled = true;
        otpInput.disabled = true;
        resendBtn.disabled = false;
        resendText.textContent = 'Gửi lại OTP';

        // Clear interval
        if (timerInterval) {
          clearInterval(timerInterval);
        }
      }
    }

    // Timer countdown
    function startTimer() {
      updateTimerDisplay();

      timerInterval = setInterval(() => {
        timeLeft--;
        updateTimerDisplay();
      }, 1000);
    }

    // Khởi động timer nếu còn thời gian
    if (timeLeft > 0) {
      startTimer();
    } else {
      updateTimerDisplay(); // Hiển thị trạng thái hết hạn
    }

    // Resend OTP
    resendBtn.addEventListener('click', function() {
      if (!this.disabled) {
        // Show loading
        const originalText = resendText.textContent;
        resendText.textContent = 'Đang gửi...';
        this.disabled = true;

        // Gửi request resend OTP
        fetch('${pageContext.request.contextPath}/resend-forgot-otp', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          }
        })
                .then(response => {
                  if (!response.ok) {
                    throw new Error('Network response was not ok');
                  }
                  return response.json();
                })
                .then(data => {
                  if (data.success) {
                    // Reload trang để nhận timer mới
                    window.location.reload();
                  } else {
                    alert('Gửi lại thất bại: ' + data.message);
                    resendText.textContent = originalText;
                    this.disabled = false;
                  }
                })
                .catch(error => {
                  console.error('Error:', error);
                  alert('Có lỗi xảy ra khi gửi lại OTP');
                  resendText.textContent = originalText;
                  this.disabled = false;
                });
      }
    });

    // Validate OTP form
    document.getElementById('otpForm').addEventListener('submit', function(e) {
      const otpValue = otpInput.value.trim();

      if (otpValue.length !== 6 || !/^\d{6}$/.test(otpValue)) {
        e.preventDefault();
        alert('Vui lòng nhập đúng 6 chữ số OTP');
        otpInput.focus();
        return false;
      }

      if (timeLeft <= 0) {
        e.preventDefault();
        alert('Mã OTP đã hết hạn. Vui lòng gửi lại mã mới.');
        return false;
      }

      return true;
    });
  });
</script>

</body>
</html>
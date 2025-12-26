<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đăng ký tài khoản | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS riêng -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/register.css">
</head>
<body>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow">
        <div class="card-header bg-primary text-white text-center">
          <h3>ĐĂNG KÝ TÀI KHOẢN</h3>
        </div>
        <div class="card-body">

          <!-- THÔNG BÁO LỖI -->
          <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="bi bi-exclamation-triangle-fill me-2"></i>
              <strong>Lỗi!</strong> ${errorMessage}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <!-- THÔNG BÁO THÀNH CÔNG -->
          <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
              <i class="bi bi-check-circle-fill me-2"></i>
              <strong>Thành công!</strong> ${successMessage}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <!-- FORM ĐĂNG KÝ -->
          <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">

            <!-- Họ và Tên -->
            <div class="row mb-3">
              <div class="col">
                <label class="form-label">Họ và Tên đệm</label>
                <input type="text" name="firstName" class="form-control" required
                       placeholder="Nguyễn Văn" value="${dto.firstName}">
              </div>
              <div class="col">
                <label class="form-label">Tên</label>
                <input type="text" name="lastName" class="form-control" required
                       placeholder="A" value="${dto.lastName}">
              </div>
            </div>

            <!-- Email với nút gửi OTP -->
            <div class="mb-3">
              <label class="form-label">Email</label>
              <div class="input-group">
                <input type="email" name="email" id="email" class="form-control" required
                       placeholder="example@email.com" value="${dto.email}">
                <button type="button" class="btn btn-outline-primary" id="sendOtpBtn">
                  <i class="bi bi-send me-1"></i>
                  <span id="btnText">Gửi OTP</span>
                </button>
              </div>
              <small class="text-muted">Nhấn "Gửi OTP" để nhận mã xác thực qua email</small>
            </div>

            <!-- Ô nhập OTP (ẩn mặc định, nhưng giữ lại khi có lỗi) -->
            <div class="mb-3" id="otpSection" style="display: ${not empty errorMessage and not empty dto.email ? 'block' : 'none'};">
              <label class="form-label">Mã OTP</label>
              <input type="text" name="otp" id="otpInput" class="form-control"
                     placeholder="Nhập 6 số OTP" maxlength="6" pattern="[0-9]{6}">
              <small id="otpTimer" class="text-muted"></small>
            </div>

            <!-- Mật khẩu -->
            <div class="mb-3">
              <label class="form-label">Mật khẩu</label>
              <input type="password" name="password" class="form-control" required
                     minlength="6" placeholder="Tối thiểu 6 ký tự">
            </div>

            <!-- Số điện thoại -->
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input type="tel" name="phone" class="form-control"
                     placeholder="0123456789" value="${dto.phone}">
            </div>

            <!-- Ngày sinh -->
            <div class="mb-3">
              <label class="form-label">Ngày sinh</label>
              <input type="date" name="dateOfBirth" class="form-control" value="${dto.dateOfBirth}">
            </div>

            <!-- Giới tính -->
            <div class="mb-3">
              <label class="form-label">Giới tính</label>
              <div class="mt-1">
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="male"
                         value="Male" ${empty dto.gender or dto.gender == 'Male' ? 'checked' : ''}>
                  <label class="form-check-label" for="male">Nam</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="female"
                         value="Female" ${dto.gender == 'Female' ? 'checked' : ''}>
                  <label class="form-check-label" for="female">Nữ</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="other"
                         value="Other" ${dto.gender == 'Other' ? 'checked' : ''}>
                  <label class="form-check-label" for="other">Khác</label>
                </div>
              </div>
            </div>

            <!-- Buttons -->
            <div class="d-grid gap-2">
              <button type="submit" class="btn btn-primary" id="registerBtn" disabled>
                <i class="bi bi-person-plus me-2"></i>
                Đăng Ký Ngay
              </button>
              <button type="reset" class="btn btn-secondary">
                <i class="bi bi-arrow-counterclockwise me-2"></i>
                Nhập lại
              </button>
            </div>
          </form>

          <!-- Links -->
          <div class="text-center mt-3">
            <span>Đã có tài khoản? </span>
            <a href="${pageContext.request.contextPath}/login" class="fw-bold">Đăng nhập ngay</a>
          </div>
          <div class="text-center mt-2">
            <a href="${pageContext.request.contextPath}/" class="text-muted">
              <i class="bi bi-house-door me-1"></i>
              Quay về trang chủ
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script xử lý OTP -->
<script>
  let otpVerified = false;
  let countdownTimer = null;
  const OTP_DURATION = 60; // 60 giây

  // Nút gửi OTP
  document.getElementById('sendOtpBtn').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const btn = this;
    const btnText = document.getElementById('btnText');

    if (!email) {
      alert('Vui lòng nhập email!');
      return;
    }

    // Disable button
    btn.disabled = true;
    btnText.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Đang gửi...';

    // Gửi request AJAX
    fetch('${pageContext.request.contextPath}/send-otp', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: 'email=' + encodeURIComponent(email)
    })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                // Hiển thị ô nhập OTP
                document.getElementById('otpSection').style.display = 'block';
                document.getElementById('otpInput').value = ''; // Xóa OTP cũ
                document.getElementById('otpInput').focus();

                // Countdown 60 giây
                startCountdown(OTP_DURATION, btn, btnText);

                // Hiển thị thông báo
                showAlert('success', '✓ Mã OTP đã được gửi đến email của bạn!');
              } else {
                alert('✗ Gửi OTP thất bại: ' + data.message);
                btn.disabled = false;
                btnText.textContent = 'Gửi OTP';
              }
            })
            .catch(error => {
              alert('✗ Lỗi kết nối server!');
              btn.disabled = false;
              btnText.textContent = 'Gửi OTP';
            });
  });

  // Countdown timer
  function startCountdown(seconds, btn, btnText) {
    const timerEl = document.getElementById('otpTimer');
    let remaining = seconds;

    // Clear timer cũ nếu có
    if (countdownTimer) {
      clearInterval(countdownTimer);
    }

    // Hàm update timer
    function updateTimer() {
      if (remaining > 0) {
        // Cập nhật timer dưới ô OTP
        timerEl.className = 'text-success';
        timerEl.innerHTML = `<i class="bi bi-clock me-1"></i>Mã có hiệu lực trong <strong>${remaining}s</strong>`;

        // Cập nhật text nút gửi OTP
        btnText.textContent = `Chờ ${remaining}s`;

        remaining--;
      } else {
        clearInterval(countdownTimer);
        // Cập nhật timer dưới ô OTP khi hết hạn
        timerEl.className = 'text-danger';
        timerEl.innerHTML = '<i class="bi bi-exclamation-triangle me-1"></i>Mã OTP đã hết hạn! Vui lòng gửi lại.';

        // Cập nhật nút gửi OTP khi hết hạn
        btn.disabled = false;
        btnText.textContent = 'Gửi lại OTP';

        otpVerified = false;
        document.getElementById('registerBtn').disabled = true;
      }
    }

    // Chạy update ngay lập tức để hiển thị 60s đầu tiên
    updateTimer();

    // Sau đó chạy interval mỗi 1s
    countdownTimer = setInterval(updateTimer, 1000);
  }

  // Kiểm tra OTP khi user nhập
  document.getElementById('otpInput').addEventListener('input', function() {
    const otp = this.value;
    const registerBtn = document.getElementById('registerBtn');

    if (otp.length === 6 && /^\d{6}$/.test(otp)) {
      // Enable nút đăng ký
      registerBtn.disabled = false;
      otpVerified = true;
      this.classList.remove('is-invalid');
      this.classList.add('is-valid');
    } else {
      registerBtn.disabled = true;
      otpVerified = false;
      if (otp.length > 0) {
        this.classList.add('is-invalid');
        this.classList.remove('is-valid');
      } else {
        this.classList.remove('is-invalid', 'is-valid');
      }
    }
  });

  // Validate form trước khi submit
  document.getElementById('registerForm').addEventListener('submit', function(e) {
    if (!otpVerified) {
      e.preventDefault();
      alert('Vui lòng nhập đúng mã OTP (6 số) trước khi đăng ký!');
      document.getElementById('otpInput').focus();
    }
  });

  // Hiển thị alert động
  function showAlert(type, message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.querySelector('.card-body').insertBefore(alertDiv, document.querySelector('form'));

    // Tự động ẩn sau 5s
    setTimeout(() => {
      alertDiv.remove();
    }, 5000);
  }

  // Nếu có lỗi OTP từ server, giữ ô OTP hiển thị
  <c:if test="${not empty errorMessage and not empty dto.email}">
  document.getElementById('otpSection').style.display = 'block';
  document.getElementById('otpInput').focus();
  </c:if>
</script>

</body>
</html>
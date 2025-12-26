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
  const OTP_DURATION = 90;

  // ✅ Kiểm tra xem có OTP đang chạy không (khi reload sau khi nhập sai)
  <c:if test="${not empty errorMessage and not empty dto.email and not empty sessionScope.otpTime}">
  (function() {
    const otpTime = ${sessionScope.otpTime};
    const currentTime = Date.now();
    const elapsed = Math.floor((currentTime - otpTime) / 1000); // Thời gian đã trôi qua (giây)
    const timeLeft = OTP_DURATION - elapsed;

    if (timeLeft > 0) {
      // Còn thời gian → Tiếp tục countdown
      const btn = document.getElementById('sendOtpBtn');
      const btnText = document.getElementById('btnText');
      startCountdown(timeLeft, btn, btnText);
    } else {
      // Hết thời gian → Hiển thị thông báo hết hạn
      const timerEl = document.getElementById('otpTimer');
      timerEl.innerHTML = '';
      const icon = document.createElement('i');
      icon.className = 'bi bi-exclamation-triangle';
      const text = document.createTextNode('Mã OTP đã hết hạn! Vui lòng gửi lại.');
      timerEl.appendChild(icon);
      timerEl.appendChild(text);
      timerEl.className = 'text-danger';
    }
  })();
  </c:if>

  document.getElementById('sendOtpBtn').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const btn = this;
    const btnText = document.getElementById('btnText');

    if (!email) {
      alert('Vui lòng nhập email!');
      return;
    }

    btn.disabled = true;
    btnText.textContent = 'Đang gửi...';

    fetch('${pageContext.request.contextPath}/send-otp', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: 'email=' + encodeURIComponent(email)
    })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                document.getElementById('otpSection').style.display = 'block';
                document.getElementById('otpInput').value = '';
                document.getElementById('otpInput').focus();

                startCountdown(OTP_DURATION, btn, btnText);
                showAlert('success', 'Mã OTP đã được gửi đến email của bạn!');
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

  function startCountdown(duration, btn, btnText) {
    const timerEl = document.getElementById('otpTimer');
    let timeLeft = duration;

    if (countdownTimer) {
      clearInterval(countdownTimer);
    }

    function updateDisplay() {
      timerEl.innerHTML = '';

      const icon = document.createElement('i');
      icon.className = 'bi bi-clock';

      const text = document.createTextNode('Mã có hiệu lực trong ');

      const num = document.createElement('span');
      num.textContent = timeLeft + 's';

      timerEl.appendChild(icon);
      timerEl.appendChild(text);
      timerEl.appendChild(num);
      timerEl.className = 'text-success';

      btn.disabled = true;
      btnText.textContent = timeLeft + 's';
    }

    updateDisplay();

    countdownTimer = setInterval(() => {
      timeLeft--;

      if (timeLeft > 0) {
        updateDisplay();
      } else {
        clearInterval(countdownTimer);

        timerEl.innerHTML = '';
        const icon = document.createElement('i');
        icon.className = 'bi bi-exclamation-triangle';
        const text = document.createTextNode('Mã OTP đã hết hạn! Vui lòng gửi lại.');
        timerEl.appendChild(icon);
        timerEl.appendChild(text);
        timerEl.className = 'text-danger';

        btn.disabled = false;
        btnText.textContent = 'Gửi lại OTP';

        document.getElementById('registerBtn').disabled = true;
        otpVerified = false;
      }
    }, 1000);
  }

  document.getElementById('otpInput').addEventListener('input', function() {
    const otp = this.value;
    const registerBtn = document.getElementById('registerBtn');

    if (otp.length === 6 && /^\d{6}$/.test(otp)) {
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

  document.getElementById('registerForm').addEventListener('submit', function(e) {
    if (!otpVerified) {
      e.preventDefault();
      alert('Vui lòng nhập đúng mã OTP (6 số) trước khi đăng ký!');
      document.getElementById('otpInput').focus();
    }
  });

  function showAlert(type, message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.setAttribute('role', 'alert');

    const icon = document.createElement('i');
    icon.className = type === 'success' ? 'bi bi-check-circle-fill me-2' : 'bi bi-exclamation-triangle-fill me-2';

    const textNode = document.createTextNode(message);

    const closeBtn = document.createElement('button');
    closeBtn.type = 'button';
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('data-bs-dismiss', 'alert');
    closeBtn.setAttribute('aria-label', 'Close');

    alertDiv.appendChild(icon);
    alertDiv.appendChild(textNode);
    alertDiv.appendChild(closeBtn);

    document.querySelector('.card-body').insertBefore(alertDiv, document.querySelector('form'));

    setTimeout(() => alertDiv.remove(), 5000);
  }
</script>

</body>
</html>
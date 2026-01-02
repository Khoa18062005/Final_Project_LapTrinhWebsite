<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Đặt Lại Mật Khẩu | VietTech</title>
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
      max-width: 450px;
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
    .password-strength {
      height: 5px;
      border-radius: 2px;
      margin-top: 5px;
      transition: all 0.3s;
      opacity: 0;
      visibility: hidden;
      transform-origin: left;
    }
    .password-strength.show {
      opacity: 1;
      visibility: visible;
    }
    .strength-weak { background-color: #dc3545; width: 25%; }
    .strength-fair { background-color: #ffc107; width: 50%; }
    .strength-good { background-color: #198754; width: 75%; }
    .strength-strong { background-color: #0d6efd; width: 100%; }
    .card-header h3 {
      font-weight: 700;
      margin-bottom: 0.5rem;
    }
    .card-header p {
      opacity: 0.9;
      font-size: 0.95rem;
    }
    .input-group-text {
      cursor: pointer;
    }
  </style>
</head>
<body>

<div class="card">
  <div class="card-header">
    <h3>Đặt Lại Mật Khẩu</h3>
    <p class="mb-0">Tạo mật khẩu mới cho tài khoản của bạn</p>
  </div>
  <div class="card-body">

    <!-- THÔNG BÁO LỖI -->
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Lỗi!</strong> ${errorMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/reset-password" method="post" id="resetForm">

      <!-- Mật khẩu mới -->
      <div class="mb-4">
        <label for="newPassword" class="form-label">Mật khẩu mới</label>
        <div class="input-group">
          <input type="password"
                 class="form-control"
                 id="newPassword"
                 name="newPassword"
                 placeholder="Nhập mật khẩu mới"
                 required
                 autofocus
                 minlength="6">
          <button class="btn btn-outline-secondary" type="button" id="toggleNewPassword">
            Hiện
          </button>
        </div>
        <div class="password-strength" id="passwordStrength"></div>
        <small class="text-muted">Tối thiểu 6 ký tự</small>
      </div>

      <!-- Xác nhận mật khẩu -->
      <div class="mb-4">
        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
        <div class="input-group">
          <input type="password"
                 class="form-control"
                 id="confirmPassword"
                 name="confirmPassword"
                 placeholder="Nhập lại mật khẩu mới"
                 required
                 minlength="6">
          <button class="btn btn-outline-secondary" type="button" id="toggleConfirmPassword">
            Hiện
          </button>
        </div>
        <div class="mt-2" id="passwordMatch"></div>
      </div>

      <!-- Nút đặt lại mật khẩu -->
      <button type="submit" class="btn btn-primary w-100" id="submitBtn">
        Đặt Lại Mật Khẩu
      </button>
    </form>

    <!-- Các liên kết phụ -->
    <div class="text-center mt-4">
      <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
        Quay lại đăng nhập
      </a>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const toggleNewPassword = document.getElementById('toggleNewPassword');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const passwordStrength = document.getElementById('passwordStrength');
    const passwordMatch = document.getElementById('passwordMatch');
    const submitBtn = document.getElementById('submitBtn');

    // Toggle hiển thị mật khẩu
    toggleNewPassword.addEventListener('click', function() {
      const type = newPassword.getAttribute('type') === 'password' ? 'text' : 'password';
      newPassword.setAttribute('type', type);
      this.textContent = type === 'password' ? 'Hiện' : 'Ẩn';
    });

    toggleConfirmPassword.addEventListener('click', function() {
      const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
      confirmPassword.setAttribute('type', type);
      this.textContent = type === 'password' ? 'Hiện' : 'Ẩn';
    });

    // Kiểm tra độ mạnh mật khẩu
    newPassword.addEventListener('input', function() {
      const password = this.value;

      // Nếu password rỗng, ẩn thanh strength
      if (password.length === 0) {
        passwordStrength.classList.remove('show');
        passwordStrength.className = 'password-strength';
        passwordStrength.title = '';
        checkPasswordMatch();
        return;
      }

      // Nếu có ký tự, hiện thanh strength
      passwordStrength.classList.add('show');

      let strength = 0;
      let strengthText = '';
      let strengthClass = '';

      // Kiểm tra độ dài
      if (password.length >= 6) strength += 1;
      if (password.length >= 8) strength += 1;

      // Kiểm tra chữ hoa
      if (/[A-Z]/.test(password)) strength += 1;

      // Kiểm tra số
      if (/[0-9]/.test(password)) strength += 1;

      // Kiểm tra ký tự đặc biệt
      if (/[^A-Za-z0-9]/.test(password)) strength += 1;

      // Xác định độ mạnh
      if (strength < 2) {
        strengthText = 'Yếu';
        strengthClass = 'strength-weak';
      } else if (strength < 3) {
        strengthText = 'Trung bình';
        strengthClass = 'strength-fair';
      } else if (strength < 4) {
        strengthText = 'Tốt';
        strengthClass = 'strength-good';
      } else {
        strengthText = 'Mạnh';
        strengthClass = 'strength-strong';
      }

      passwordStrength.className = 'password-strength show ' + strengthClass;
      passwordStrength.title = 'Độ mạnh: ' + strengthText;

      // Kiểm tra trùng khớp
      checkPasswordMatch();
    });

    // Kiểm tra mật khẩu trùng khớp
    confirmPassword.addEventListener('input', checkPasswordMatch);

    function checkPasswordMatch() {
      const pass1 = newPassword.value;
      const pass2 = confirmPassword.value;

      // Reset nếu cả hai ô đều trống
      if (pass1.length === 0 && pass2.length === 0) {
        passwordMatch.innerHTML = '';
        submitBtn.disabled = true;
        return;
      }

      if (pass2.length === 0) {
        passwordMatch.innerHTML = '';
        submitBtn.disabled = true;
        return;
      }

      if (pass1 === pass2 && pass1.length >= 6) {
        passwordMatch.innerHTML = '<span class="text-success">✓ Mật khẩu trùng khớp</span>';
        submitBtn.disabled = false;
      } else if (pass1 !== pass2) {
        passwordMatch.innerHTML = '<span class="text-danger">✗ Mật khẩu không khớp</span>';
        submitBtn.disabled = true;
      } else {
        passwordMatch.innerHTML = '';
        submitBtn.disabled = true;
      }
    }

    // Validate form submit
    document.getElementById('resetForm').addEventListener('submit', function(e) {
      if (newPassword.value !== confirmPassword.value) {
        e.preventDefault();
        alert('Mật khẩu không khớp!');
        return false;
      }

      if (newPassword.value.length < 6) {
        e.preventDefault();
        alert('Mật khẩu phải có ít nhất 6 ký tự!');
        return false;
      }

      return true;
    });
  });
</script>

</body>
</html>
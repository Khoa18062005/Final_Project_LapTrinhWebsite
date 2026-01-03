<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="context-path" content="${pageContext.request.contextPath}">
  <title>Đăng ký tài khoản | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- CSS riêng -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/register.css">
</head>
<body>

<!-- Hidden input để JavaScript đọc otpTime -->
<c:if test="${not empty sessionScope.otpTime and not empty errorMessage}">
  <input type="hidden" id="otpTimeData" value="${sessionScope.otpTime}">
</c:if>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-4">
      <div class="card shadow">
        <div class="card-header text-white text-center">
          <h3>Đăng ký tài khoản</h3>
        </div>
        <div class="card-body">

          <!-- THÔNG BÁO LỖI -->
          <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <strong>Lỗi:</strong> ${errorMessage}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <!-- THÔNG BÁO THÀNH CÔNG -->
          <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
              <strong>Thành công:</strong> ${successMessage}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <!-- FORM ĐĂNG KÝ -->
          <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">

            <!-- Họ và Tên -->
            <div class="row mb-3">
              <div class="col">
                <label class="form-label">Họ và tên đệm</label>
                <input type="text" name="firstName" class="form-control" required
                       placeholder="Nguyễn Văn" value="${dto.firstName}">
              </div>
              <div class="col">
                <label class="form-label">Tên</label>
                <input type="text" name="lastName" class="form-control" required
                       placeholder="An" value="${dto.lastName}">
              </div>
            </div>

            <!-- Email với nút gửi OTP -->
            <div class="mb-3">
              <label class="form-label">Email</label>
              <div class="input-group">
                <input type="email" name="email" id="email" class="form-control" required
                       placeholder="example@email.com" value="${dto.email}">
                <button type="button" class="btn btn-outline-primary" id="sendOtpBtn">
                  <span id="btnText">Gửi mã</span>
                </button>
              </div>
              <small class="text-muted">Nhấn "Gửi mã" để nhận mã xác thực qua email</small>
              <div class="invalid-feedback" id="emailFeedback"></div>
            </div>

            <!-- Ô nhập OTP -->
            <div class="mb-3" id="otpSection" style="display: ${not empty errorMessage and not empty dto.email ? 'block' : 'none'};">
              <label class="form-label">Mã xác thực</label>
              <input type="text" name="otp" id="otpInput" class="form-control"
                     placeholder="Nhập 6 chữ số" maxlength="6" pattern="[0-9]{6}">
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
              <div class="mt-2">
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

            <!-- Mã giới thiệu (KHÔNG BẮT BUỘC) -->
            <div class="mb-3">
              <label class="form-label">Mã giới thiệu <span class="text-muted small">(Không bắt buộc)</span></label>
              <input type="text" name="referralCode" class="form-control"
                     placeholder="Nhập mã giới thiệu nếu có" value="${dto.referralCode}"
                     maxlength="20">
              <small class="text-muted">Nếu bạn có mã giới thiệu từ người dùng khác, hãy nhập vào đây</small>
            </div>

            <!-- Buttons -->
            <div class="d-grid gap-2">
              <button type="submit" class="btn btn-primary" id="registerBtn" disabled>
                Đăng ký
              </button>
              <button type="reset" class="btn btn-secondary">
                Nhập lại
              </button>
            </div>
          </form>

          <!-- Links -->
          <div class="text-center mt-3">
            <span>Đã có tài khoản? </span>
            <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
          </div>
          <div class="text-center mt-2">
            <a href="${pageContext.request.contextPath}/" class="text-muted">
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

<!-- Script riêng -->
<script src="${pageContext.request.contextPath}/assets/js/register.js"></script>

</body>
</html>
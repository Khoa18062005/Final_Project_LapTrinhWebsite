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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reset-password.css">


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

<script src="${pageContext.request.contextPath}/assets/js/reset-password.js"></script>


</body>
</html>
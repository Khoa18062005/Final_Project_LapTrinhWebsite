<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng Nhập | VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS tùy chỉnh -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>

<div class="card">
    <div class="card-header">
        <h3>ĐĂNG NHẬP</h3>
    </div>

    <div class="card-body">

        <!-- THÔNG BÁO LỖI (nếu có) -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <strong>Lỗi!</strong> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- THÔNG BÁO THÀNH CÔNG (nếu có - từ redirect) -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <strong>Thành công!</strong> ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <form action="login" method="post">

            <!-- Email -->
            <div class="mb-4">
                <label for="email" class="form-label">Email</label>
                <input type="email"
                       class="form-control ${not empty errorMessage ? 'is-invalid' : ''}"
                       id="email"
                       name="email"
                       value="${not empty email ? email : ''}"
                       placeholder="nhập email của bạn"
                       required
                       autofocus>
            </div>

            <!-- Mật khẩu -->
            <div class="mb-4">
                <label for="password" class="form-label">Mật khẩu</label>
                <input type="password"
                       class="form-control ${not empty errorMessage ? 'is-invalid' : ''}"
                       id="password"
                       name="password"
                       placeholder="nhập mật khẩu"
                       required>
            </div>

            <!-- Nút đăng nhập -->
            <button type="submit" class="btn btn-primary w-100">
                <i class="bi bi-box-arrow-in-right me-2"></i>
                ĐĂNG NHẬP
            </button>
        </form>

        <!-- Các liên kết phụ -->
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none">
                <i class="bi bi-question-circle me-1"></i>
                Quên mật khẩu?
            </a>
        </div>
        <div class="text-center mt-2">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register" class="fw-bold text-decoration-none">
                Đăng ký ngay
            </a>
        </div>

        <!-- Link về trang chủ -->
        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/" class="text-muted text-decoration-none">
                <i class="bi bi-house-door me-1"></i>
                Quay về trang chủ
            </a>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
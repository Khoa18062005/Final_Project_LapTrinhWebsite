<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quên Mật Khẩu | VietTech</title>
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
        .text-center a {
            text-decoration: none;
        }
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
        <h3>Quên Mật Khẩu</h3>
        <p class="mb-0">Nhập email để nhận mã OTP đặt lại mật khẩu</p>
    </div>
    <div class="card-body">

        <!-- THÔNG BÁO LỖI -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Lỗi!</strong> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- THÔNG BÁO THÀNH CÔNG -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>Thành công!</strong> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password" method="post">

            <!-- Email -->
            <div class="mb-4">
                <label for="email" class="form-label">Email đăng ký tài khoản</label>
                <input type="email"
                       class="form-control"
                       id="email"
                       name="email"
                       placeholder="Nhập email của bạn"
                       required
                       autofocus>
                <small class="text-muted">Chúng tôi sẽ gửi mã OTP đến email này</small>
            </div>

            <!-- Nút gửi OTP -->
            <button type="submit" class="btn btn-primary w-100">
                Gửi mã OTP
            </button>
        </form>

        <!-- Các liên kết phụ -->
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                Quay lại đăng nhập
            </a>
        </div>
        <div class="text-center mt-2">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register" class="fw-bold text-decoration-none">
                Đăng ký ngay
            </a>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
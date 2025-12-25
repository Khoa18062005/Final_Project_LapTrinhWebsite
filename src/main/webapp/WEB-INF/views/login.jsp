<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng Nhập</title>

    <!-- Bootstrap 5 Grid chỉ (không dùng style để tránh xung đột) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS tùy chỉnh -->
    <link rel="stylesheet" href="assets/css/login.css">
</head>
<body>

<div class="card">
    <div class="card-header">
        <h3>ĐĂNG NHẬP</h3>
    </div>

    <div class="card-body">
        <form action="login" method="post">

            <!-- Email -->
            <div class="mb-4">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email"
                       placeholder="nhập email của bạn" required autofocus>
            </div>

            <!-- Mật khẩu -->
            <div class="mb-4">
                <label for="password" class="form-label">Mật khẩu</label>
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="nhập mật khẩu" required>
            </div>

            <!-- Nút đăng nhập -->
            <button type="submit" class="btn btn-primary">ĐĂNG NHẬP</button>
        </form>

        <!-- Các liên kết phụ -->
        <div class="text-center mt-4">
            <a href="#">Quên mật khẩu?</a>
        </div>
        <div class="text-center mt-2">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </div>
    </div>
</div>

</body>
</html>

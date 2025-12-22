<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Đăng ký tài khoản</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="style_register.css">
</head>
<body class="bg-light">

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow">
        <div class="card-header bg-primary text-white text-center">
          <h3>ĐĂNG KÝ TÀI KHOẢN</h3>
        </div>
        <div class="card-body">
          <form action="/register" method="POST">

            <div class="row mb-3">
              <div class="col">
                <label class="form-label">Họ và Tên đệm</label>
                <input type="text" name="firstName" class="form-label control" required placeholder="Nguyễn Văn">
              </div>
              <div class="col">
                <label class="form-label">Tên</label>
                <input type="text" name="lastName" class="form-label control" required placeholder="A">
              </div>
            </div>

            <div class="mb-3">
              <label class="form-label">Mật khẩu</label>
              <input type="password" name="password" class="form-control" required>
            </div>

            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" name="email" class="form-control" required placeholder="">
            </div>

            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input type="tel" name="phone" class="form-control">
            </div>

<%--            <div class="mb-3">--%>
<%--              <label class="form-label">Đường dẫn ảnh đại diện (Avatar URL)</label>--%>
<%--              <input type="text" name="avatar" class="form-control" placeholder="http://image-url.com/avatar.jpg">--%>
<%--            </div>--%>

            <div class="mb-3">
              <label class="form-label">Ngày sinh</label>
              <input type="date" name="dateOfBirth" class="form-control">
            </div>

            <div class="mb-3">
              <label class="form-label">Giới tính</label>
              <div class="mt-1">
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="male" value="Male" checked>
                  <label class="form-check-label" for="male">Nam</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="female" value="Female">
                  <label class="form-check-label" for="female">Nữ</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="gender" id="other" value="Other">
                  <label class="form-check-label" for="other">Khác</label>
                </div>
              </div>
            </div>

            <div class="d-grid gap-2">
              <button type="submit" class="btn btn-primary">Đăng Ký Ngay</button>
              <button type="reset" class="btn btn-secondary">Nhập lại</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

</body>
</html>
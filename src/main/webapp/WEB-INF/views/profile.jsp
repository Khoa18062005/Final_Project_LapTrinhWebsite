<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hồ Sơ Của Tôi | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS chung -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <!-- CSS riêng cho profile -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
</head>
<body>

<!-- HEADER (giống trang chủ) -->
<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
        <img src="${pageContext.request.contextPath}/assets/PNG/LogoVT.png"
             alt="VietTech Logo"
             height="60"
             class="d-inline-block align-text-top">
      </a>

      <form class="d-flex w-50 mx-3">
        <input class="form-control me-2" type="search" placeholder="Hôm nay bạn muốn tìm gì...">
        <button class="btn-search" type="submit">
          <i class="bi bi-search text-dark"></i>
        </button>
      </form>

      <div class="header-right-items">
        <div class="items-header">
          <i class="bi bi-cart3 fs-4 text-white"></i>
          <h5>Giỏ hàng</h5>
        </div>

        <c:if test="${not empty sessionScope.user}">
          <div class="items-header dropdown">
            <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown"
                    data-bs-toggle="dropdown" aria-expanded="false">
              <i class="bi bi-person-circle me-1"></i>
              <span class="user-name">
                                ${sessionScope.user.firstName} ${sessionScope.user.lastName}
                            </span>
            </button>
            <ul class="dropdown-menu" aria-labelledby="userDropdown">
              <li><a class="dropdown-item active" href="${pageContext.request.contextPath}/profile">
                <i class="bi bi-person"></i> Tài khoản cá nhân
              </a></li>
              <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                <i class="bi bi-box"></i> Đơn hàng của tôi
              </a></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                <i class="bi bi-box-arrow-right"></i> Đăng xuất
              </a></li>
            </ul>
          </div>
        </c:if>
      </div>
    </div>
  </nav>
</header>

<!-- THÔNG BÁO -->
<%
  String successMessage = (String) session.getAttribute("successMessage");
  String errorMessage = (String) session.getAttribute("errorMessage");
%>

<% if (successMessage != null && !successMessage.isEmpty()) { %>
<div class="alert-container">
  <div class="container">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      <i class="bi bi-check-circle-fill me-2"></i>
      <strong>Thành công!</strong> <%= successMessage %>
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </div>
</div>
<% session.removeAttribute("successMessage"); %>
<% } %>

<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
<div class="alert-container">
  <div class="container">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      <i class="bi bi-exclamation-triangle-fill me-2"></i>
      <strong>Lỗi!</strong> <%= errorMessage %>
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </div>
</div>
<% session.removeAttribute("errorMessage"); %>
<% } %>

<!-- MAIN CONTENT -->
<div class="container mt-4 mb-5">
  <div class="row">
    <!-- SIDEBAR -->
    <div class="col-md-3">
      <div class="profile-sidebar">
        <!-- User info -->
        <div class="user-info-box">
          <div class="user-avatar">
            <img src="${pageContext.request.contextPath}/assets/images/default-avatar.png"
                 alt="Avatar"
                 id="avatarPreview">
          </div>
          <div class="user-name-box">
            <h6 class="mb-0">${user.firstName} ${user.lastName}</h6>
            <small class="text-muted">
              <i class="bi bi-pencil"></i> Sửa Hồ Sơ
            </small>
          </div>
        </div>

        <!-- Menu -->
        <div class="profile-menu">
          <div class="menu-item active">
            <i class="bi bi-person text-primary"></i>
            <span>Tài Khoản Của Tôi</span>
          </div>
          <div class="menu-sub-item active">
            <span>Hồ Sơ</span>
          </div>
          <div class="menu-sub-item">
            <span>Ngân Hàng</span>
          </div>
          <div class="menu-sub-item">
            <span>Địa Chỉ</span>
          </div>
          <div class="menu-sub-item">
            <span>Đổi Mật Khẩu</span>
          </div>
          <div class="menu-sub-item">
            <span>Cài Đặt Thông Báo</span>
          </div>
          <div class="menu-sub-item">
            <span>Những Thiết Lập Riêng Tư</span>
          </div>

          <div class="menu-item">
            <i class="bi bi-box"></i>
            <span>Đơn Mua</span>
          </div>

          <div class="menu-item">
            <i class="bi bi-bell"></i>
            <span>Thông Báo</span>
          </div>

          <div class="menu-item">
            <i class="bi bi-ticket-perforated"></i>
            <span>Kho Voucher</span>
          </div>

        </div>
      </div>
    </div>

    <!-- MAIN CONTENT -->
    <div class="col-md-9">
      <div class="profile-content">
        <div class="profile-header">
          <h4>Hồ Sơ Của Tôi</h4>
          <p class="text-muted">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
        </div>

        <form action="${pageContext.request.contextPath}/profile" method="POST" id="profileForm">
          <input type="hidden" name="action" value="update_info">

          <div class="row">
            <!-- Left column - Form -->
            <div class="col-md-8">
              <!-- Tên đăng nhập -->
              <div class="form-group-custom">
                <label>Tên đăng nhập</label>
                <div class="form-value">
                  <span class="text-dark">${user.username}</span>
                  <small class="text-muted d-block">
                    Tên Đăng nhập chỉ có thể thay đổi một lần.
                  </small>
                </div>
              </div>

              <!-- Tên -->
              <div class="form-group-custom">
                <label>Tên</label>
                <input type="text"
                       class="form-control-custom"
                       name="firstName"
                       value="${user.firstName}"
                       placeholder="Nhập tên">
              </div>

              <!-- Họ -->
              <div class="form-group-custom">
                <label>Họ</label>
                <input type="text"
                       class="form-control-custom"
                       name="lastName"
                       value="${user.lastName}"
                       placeholder="Nhập họ">
              </div>

              <!-- Email -->
              <div class="form-group-custom">
                <label>Email</label>
                <div class="form-value">
                  <span class="text-dark">${user.email}</span>
                  <a href="#" class="text-primary ms-2">Thay Đổi</a>
                </div>
              </div>

              <!-- Số điện thoại -->
              <div class="form-group-custom">
                <label>Số điện thoại</label>
                <div class="input-group">
                  <input type="tel"
                         class="form-control-custom"
                         name="phone"
                         value="${user.phone}"
                         placeholder="Nhập số điện thoại">
                  <button type="button" class="btn btn-outline-primary btn-sm ms-2">
                    Thêm
                  </button>
                </div>
              </div>

              <!-- Giới tính -->
              <div class="form-group-custom">
                <label>Giới tính</label>
                <div class="gender-options">
                  <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="gender"
                           id="male" value="Nam"
                    ${user.gender == 'Nam' ? 'checked' : ''}>
                    <label class="form-check-label" for="male">Nam</label>
                  </div>
                  <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="gender"
                           id="female" value="Nữ"
                    ${user.gender == 'Nữ' ? 'checked' : ''}>
                    <label class="form-check-label" for="female">Nữ</label>
                  </div>
                  <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="gender"
                           id="other" value="Khác"
                    ${user.gender == 'Khác' ? 'checked' : ''}>
                    <label class="form-check-label" for="other">Khác</label>
                  </div>
                </div>
              </div>

              <!-- Ngày sinh -->
              <div class="form-group-custom">
                <label>Ngày sinh</label>
                <div class="row g-2">
                  <div class="col-4">
                    <select class="form-select-custom" name="day">
                      <option value="">Ngày</option>
                      <c:forEach begin="1" end="31" var="day">
                        <option value="${day}">${day}</option>
                      </c:forEach>
                    </select>
                  </div>
                  <div class="col-4">
                    <select class="form-select-custom" name="month">
                      <option value="">Tháng</option>
                      <c:forEach begin="1" end="12" var="month">
                        <option value="${month}">Tháng ${month}</option>
                      </c:forEach>
                    </select>
                  </div>
                  <div class="col-4">
                    <select class="form-select-custom" name="year">
                      <option value="">Năm</option>
                      <c:forEach begin="1950" end="2024" var="year">
                        <option value="${year}">${year}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
              </div>

              <!-- Button Save -->
              <div class="form-group-custom">
                <label></label>
                <button type="submit" class="btn btn-primary px-5">
                  Lưu
                </button>
              </div>
            </div>

            <!-- Right column - Avatar -->
            <div class="col-md-4">
              <div class="avatar-upload-box">
                <div class="avatar-preview-large">
                  <img src="${pageContext.request.contextPath}/assets/images/default-avatar.png"
                       alt="Avatar"
                       id="avatarPreviewLarge">
                </div>
                <button type="button" class="btn btn-outline-secondary btn-sm mt-3"
                        onclick="document.getElementById('avatarInput').click()">
                  Chọn Ảnh
                </button>
                <input type="file"
                       id="avatarInput"
                       accept="image/*"
                       style="display: none;">
                <small class="text-muted d-block mt-2">
                  Dung lượng file tối đa 1 MB<br>
                  Định dạng: .JPEG, .PNG
                </small>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>

</body>
</html>
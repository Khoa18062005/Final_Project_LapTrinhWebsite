<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hồ Sơ Của Tôi | VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS chung -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <!-- CSS riêng cho profile -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
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
                            <!-- Avatar thay vì icon -->
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.avatar and sessionScope.user.avatar != ''}">
                                    <img src="${sessionScope.user.avatar}"
                                         alt="Avatar"
                                         class="user-avatar-small me-1"
                                         onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg"
                                         alt="Avatar"
                                         class="user-avatar-small me-1">
                                </c:otherwise>
                            </c:choose>
                            <span class="user-name">
                                ${sessionScope.user.lastName} ${sessionScope.user.firstName}
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
<div class="container-fluid mt-4 mb-5">
    <div class="row">
        <!-- SIDEBAR -->
        <div class="col-lg-2 col-md-3">
            <div class="profile-sidebar">
                <!-- User info -->
                <div class="user-info-box">
                    <div class="user-avatar">
                        <c:choose>
                            <c:when test="${not empty user.avatar and user.avatar != ''}">
                                <img src="${user.avatar}"
                                     alt="Avatar"
                                     id="avatarPreview"
                                     onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg"
                                     alt="Avatar" id="avatarPreview">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="user-name-box">
                        <h6 class="mb-0">${user.lastName} ${user.firstName}</h6>
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
        <div class="col-lg-10 col-md-9">
            <div class="profile-content">
                <div class="profile-header">
                    <h4>Hồ Sơ Của Tôi</h4>
                    <p class="text-muted">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
                </div>

                <form action="${pageContext.request.contextPath}/profile"
                      method="POST"
                      id="profileForm"
                      enctype="multipart/form-data">

                    <input type="hidden" name="action" value="update_info">

                    <!-- Hidden inputs để lưu giá trị gốc -->
                    <input type="hidden" id="originalAvatar" value="${not empty user.avatar ? user.avatar : ''}">
                    <input type="hidden" id="originalGender" value="${user.gender}">

                    <div class="row">
                        <!-- Left column - Form -->
                        <div class="col-lg-8">
                            <!-- Tên đăng nhập -->
                            <div class="form-group-custom">
                                <label>Tên đăng nhập</label>
                                <div class="form-value">
                                    <span class="text-dark fw-semibold">${user.email}</span>
                                </div>
                            </div>

                            <!-- Họ -->
                            <div class="form-group-custom">
                                <label>Họ và tên đệm<span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control-custom"
                                       name="lastName"
                                       id="lastName"
                                       value="${user.lastName}"
                                       placeholder="Nhập họ"
                                       data-original="${user.lastName}"
                                       required>
                            </div>

                            <!-- Tên -->
                            <div class="form-group-custom">
                                <label>Tên <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control-custom"
                                       name="firstName"
                                       id="firstName"
                                       value="${user.firstName}"
                                       placeholder="Nhập tên"
                                       data-original="${user.firstName}"
                                       required>
                            </div>

                            <!-- Email -->
                            <div class="form-group-custom">
                                <label>Email<span class="text-danger">*</span></label>
                                <div style="flex: 1;">
                                    <input type="email"
                                           class="form-control-custom"
                                           name="email"
                                           id="email"
                                           value="${user.email}"
                                           placeholder="Nhập email"
                                           data-original="${user.email}"
                                           required>

                                    <!-- OTP SECTION (ẩn mặc định) -->
                                    <div id="emailOtpSection" style="display: none; margin-top: 12px;">
                                        <div class="d-flex gap-2 align-items-center">
                                            <input type="text"
                                                   class="form-control-custom"
                                                   id="emailOtp"
                                                   name="emailOtp"
                                                   placeholder="Nhập mã OTP"
                                                   maxlength="6"
                                                   pattern="[0-9]{6}"
                                                   style="max-width: 200px;">
                                            <button type="button"
                                                    class="btn btn-primary btn-sm"
                                                    id="sendOtpBtn">
                                                <i class="bi bi-send me-1"></i> Gửi OTP
                                            </button>
                                        </div>
                                        <small id="otpTimer" class="text-muted d-block mt-2"></small>
                                    </div>
                                </div>
                            </div>

                            <!-- Số điện thoại -->
                            <div class="form-group-custom">
                                <label>Số điện thoại</label>
                                <input type="tel"
                                       class="form-control-custom"
                                       name="phone"
                                       id="phone"
                                       value="${user.phone}"
                                       placeholder="Nhập số điện thoại"
                                       pattern="[0-9]{10,11}"
                                       data-original="${user.phone}">
                            </div>

                            <!-- Giới tính -->
                            <div class="form-group-custom">
                                <label>Giới tính</label>
                                <div class="gender-options">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input gender-radio"
                                               type="radio"
                                               name="gender"
                                               id="male"
                                               value="Male"
                                        ${user.gender eq 'Male' ? 'checked' : ''}>
                                        <label class="form-check-label" for="male">Nam</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input gender-radio"
                                               type="radio"
                                               name="gender"
                                               id="female"
                                               value="Female"
                                        ${user.gender eq 'Female' ? 'checked' : ''}>
                                        <label class="form-check-label" for="female">Nữ</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input gender-radio"
                                               type="radio"
                                               name="gender"
                                               id="other"
                                               value="Other"
                                        ${user.gender eq 'Other' ? 'checked' : ''}>
                                        <label class="form-check-label" for="other">Khác</label>
                                    </div>
                                </div>
                            </div>

                            <!-- Ngày sinh -->
                            <div class="form-group-custom">
                                <label>Ngày sinh</label>
                                <div class="row g-2">
                                    <jsp:useBean id="today" class="java.util.Date" />
                                    <fmt:formatDate value="${user.dateOfBirth}" pattern="dd" var="userDay"/>
                                    <fmt:formatDate value="${user.dateOfBirth}" pattern="MM" var="userMonth"/>
                                    <fmt:formatDate value="${user.dateOfBirth}" pattern="yyyy" var="userYear"/>

                                    <div class="col-4">
                                        <select class="form-select-custom date-select" name="day" id="daySelect" data-original="${userDay}">
                                            <option value="">Ngày</option>
                                            <c:forEach begin="1" end="31" var="day">
                                                <option value="${day}" ${day eq userDay ? 'selected' : ''}>
                                                        ${day}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-4">
                                        <select class="form-select-custom date-select" name="month" id="monthSelect" data-original="${userMonth}">
                                            <option value="">Tháng</option>
                                            <c:forEach begin="1" end="12" var="month">
                                                <option value="${month}" ${month eq userMonth ? 'selected' : ''}>
                                                        ${month}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-4">
                                        <select class="form-select-custom date-select" name="year" id="yearSelect" data-original="${userYear}">
                                            <option value="">Năm</option>
                                            <c:forEach begin="1950" end="2024" var="year">
                                                <option value="${year}" ${year eq userYear ? 'selected' : ''}>
                                                        ${year}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <!-- Button Save -->
                            <div class="form-group-custom mt-4">
                                <label></label>
                                <button type="submit" class="btn btn-primary btn-save" id="saveBtn" disabled>
                                    <i class="bi bi-check-circle me-2"></i>
                                    Lưu Thay Đổi
                                </button>
                            </div>
                        </div>

                        <!-- Right column - Avatar -->
                        <div class="col-lg-4">
                            <div class="avatar-upload-box">
                                <div class="avatar-preview-large">
                                    <c:choose>
                                        <c:when test="${not empty user.avatar and user.avatar != ''}">
                                            <img src="${user.avatar}"
                                                 alt="Avatar"
                                                 id="avatarPreviewLarge"
                                                 onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg"
                                                 alt="Avatar"
                                                 id="avatarPreviewLarge">
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <button type="button"
                                        class="btn btn-outline-secondary btn-sm mt-3"
                                        onclick="document.getElementById('avatarInput').click()">
                                    <i class="bi bi-upload me-1"></i> Chọn Ảnh
                                </button>

                                <!-- NÚT HỦY ẢNH (ẩn mặc định) -->
                                <button type="button"
                                        class="btn btn-outline-danger btn-sm mt-2"
                                        id="cancelAvatarBtn"
                                        style="display: none;">
                                    <i class="bi bi-x-circle me-1"></i> Hủy Ảnh
                                </button>

                                <input type="file"
                                       id="avatarInput"
                                       name="avatarFile"
                                       accept="image/*"
                                       style="display: none;">

                                <small class="text-muted d-block mt-2">
                                    Dung lượng file tối đa 5 MB<br>
                                    Định dạng: .JPEG, .PNG
                                </small>

                                <!-- Hiển thị tên file đã chọn -->
                                <div id="fileNameDisplay" class="mt-2 text-primary small" style="display: none;">
                                    <i class="bi bi-file-earmark-image"></i>
                                    <span id="fileName"></span>
                                </div>
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
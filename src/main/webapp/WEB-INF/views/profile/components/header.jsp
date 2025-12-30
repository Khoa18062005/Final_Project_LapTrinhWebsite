<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${pageTitle} | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="context-path" content="${pageContext.request.contextPath}">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
</head>
<body>

<!-- HEADER -->
<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
        <img src="${pageContext.request.contextPath}/assets/PNG/LogoVT.png" alt="VietTech Logo" height="60">
      </a>

      <form class="d-flex w-50 mx-3">
        <input class="form-control me-2" type="search" placeholder="Hôm nay bạn muốn tìm gì...">
        <button class="btn-search" type="submit">
          <i class="bi bi-search text-dark"></i>
        </button>
      </form>

      <div class="header-right-items">
        <!-- Chuông thông báo -->
        <div class="items-header notification-bell" id="notificationBell">
          <i class="bi bi-bell fs-4 text-white"></i>
          <h5>Thông báo</h5>
          <span class="notification-badge">0</span>
        </div>

        <div class="items-header">
          <i class="bi bi-cart3 fs-4 text-white"></i>
          <h5>Giỏ hàng</h5>
        </div>

        <c:if test="${not empty sessionScope.user}">
          <div class="items-header dropdown">
            <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown"
                    data-bs-toggle="dropdown" aria-expanded="false">
              <c:choose>
                <c:when test="${not empty sessionScope.user.avatar and sessionScope.user.avatar != ''}">
                  <img src="${sessionScope.user.avatar}" alt="Avatar" class="user-avatar-small me-1"
                       onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
                </c:when>
                <c:otherwise>
                  <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg"
                       alt="Avatar" class="user-avatar-small me-1">
                </c:otherwise>
              </c:choose>
              <span class="user-name">${sessionScope.user.firstName} ${sessionScope.user.lastName}</span>
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile/info">
                <i class="bi bi-person"></i> Tài khoản cá nhân
              </a></li>
              <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile/orders">
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
<c:if test="${not empty sessionScope.successMessage}">
<div class="alert-container">
  <div class="container">
    <div class="alert alert-success alert-dismissible fade show">
      <i class="bi bi-check-circle-fill me-2"></i>
      <strong>Thành công!</strong> ${sessionScope.successMessage}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </div>
</div>
  <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
<div class="alert-container">
  <div class="container">
    <div class="alert alert-danger alert-dismissible fade show">
      <i class="bi bi-exclamation-triangle-fill me-2"></i>
      <strong>Lỗi!</strong> ${sessionScope.errorMessage}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
  </div>
</div>
  <c:remove var="errorMessage" scope="session"/>
</c:if>

<div class="container-fluid mt-4 mb-5">
  <div class="row">
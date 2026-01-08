<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi" data-bs-theme="light">
<div class="col-lg-2 col-md-3">
  <div class="profile-sidebar">
    <!-- User info -->
    <div class="user-info-box">
      <div class="user-avatar">
        <c:choose>
          <c:when test="${not empty user.avatar and user.avatar != ''}">
            <img src="${user.avatar}" alt="Avatar"
                 onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
          </c:when>
          <c:otherwise>
            <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg" alt="Avatar">
          </c:otherwise>
        </c:choose>
      </div>
      <div class="user-name-box">
        <h6 class="mb-0">${user.firstName} ${user.lastName}</h6>
        <a href="${pageContext.request.contextPath}/profile/info" ${activePage == 'info' ? 'active' : ''}">
          <small class="text-muted">
            <i class="bi bi-pencil"></i> Sửa Hồ Sơ
          </small>
        </a>
      </div>
    </div>

    <!-- Menu -->
    <div class="profile-menu">
      <!-- Tài Khoản Của Tôi -->
      <div class="menu-item ${activePage == 'info' || activePage == 'bank' || activePage == 'address' || activePage == 'password' ? 'active' : ''}">
        <i class="bi bi-person"></i>
        <span>Tài Khoản Của Tôi</span>
      </div>

      <a href="${pageContext.request.contextPath}/profile/info" class="menu-sub-item ${activePage == 'info' ? 'active' : ''}">
        <span>Hồ Sơ</span>
      </a>
      <a href="${pageContext.request.contextPath}/profile/bank" class="menu-sub-item ${activePage == 'bank' ? 'active' : ''}">
        <span>Ngân Hàng</span>
      </a>
      <a href="${pageContext.request.contextPath}/profile/address" class="menu-sub-item ${activePage == 'address' ? 'active' : ''}">
        <span>Địa Chỉ</span>
      </a>
      <a href="${pageContext.request.contextPath}/profile/password" class="menu-sub-item ${activePage == 'password' ? 'active' : ''}">
        <span>Đổi Mật Khẩu</span>
      </a>

      <!-- Đơn Mua -->
      <a href="${pageContext.request.contextPath}/profile/orders" class="menu-item ${activePage == 'orders' ? 'active' : ''}">
        <i class="bi bi-box"></i>
        <span>Đơn Mua</span>
      </a>

      <!-- Thông Báo -->
      <a href="${pageContext.request.contextPath}/profile/notifications" class="menu-item ${activePage == 'notifications' ? 'active' : ''}">
        <i class="bi bi-bell"></i>
        <span>Thông Báo</span>
      </a>

      <!-- Kho Voucher -->
      <a href="${pageContext.request.contextPath}/profile/vouchers" class="menu-item ${activePage == 'vouchers' ? 'active' : ''}">
        <i class="bi bi-ticket-perforated"></i>
        <span>Kho Voucher</span>
      </a>

      <!-- VTX (MỚI THÊM) -->
      <a href="${pageContext.request.contextPath}/profile/vtx" class="menu-item ${activePage == 'vtx' ? 'active' : ''}">
        <i class="bi bi-coin"></i>
        <span>VTX</span>
      </a>
    </div>
  </div>
</div>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- HEADER -->
<header>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <!-- Logo -->
      <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
        <img src="${pageContext.request.contextPath}/assets/PNG/LogoVT.png"
             alt="VietTech Logo"
             height="60"
             class="d-inline-block align-text-top">
      </a>

      <!-- Ô tìm kiếm -->
      <form class="d-flex w-50 mx-3">
        <input class="form-control me-2" type="search" placeholder="Hôm nay bạn muốn tìm gì...">
        <button class="btn-search" type="submit">
          <i class="bi bi-search text-dark"></i>
        </button>
      </form>

      <!-- Nhóm các nút bên phải -->
      <div class="header-right-items">
        <!-- Thông báo -->
        <div class="notification-wrapper position-relative">
          <div class="items-header notification-bell dropdown"
               id="notificationBell"
               data-bs-toggle="dropdown"
               data-bs-auto-close="outside"
               aria-expanded="false"
               style="cursor: pointer;">
            <i class="bi bi-bell fs-4 text-white"></i>
            <h5>Thông báo</h5>

            <c:if test="${not empty sessionScope.user and headerUnreadCount > 0}">
              <span class="notification-badge">${headerUnreadCount}</span>
            </c:if>
          </div>

          <div class="dropdown-menu dropdown-menu-end notification-dropdown"
               aria-labelledby="notificationBell"
               style="min-width: 350px; max-width: 400px; padding: 0;">

            <div class="notification-dropdown-header">
              <h6 class="mb-0">Thông báo của bạn</h6>
              <small>
                <c:choose>
                  <c:when test="${not empty sessionScope.user}">
                    ${headerUnreadCount} thông báo chưa đọc
                  </c:when>
                  <c:otherwise>
                    Vui lòng đăng nhập
                  </c:otherwise>
                </c:choose>
              </small>
            </div>

            <div class="notification-dropdown-body">
              <c:choose>
                <c:when test="${not empty sessionScope.user and not empty headerNotifications}">
                  <c:forEach var="notif" items="${headerNotifications}">
                    <a href="${pageContext.request.contextPath}/notifications/quick-read?notificationId=${notif.notificationId}&redirect=profile"
                       class="notification-dropdown-item ${notif.read ? 'read' : 'unread'}">
                      <div class="notification-item-icon">
                        <c:choose>
                          <c:when test="${notif.type == 'order'}">
                            <i class="bi bi-box-seam text-primary"></i>
                          </c:when>
                          <c:when test="${notif.type == 'promotion'}">
                            <i class="bi bi-tag-fill text-success"></i>
                          </c:when>
                          <c:otherwise>
                            <i class="bi bi-info-circle text-secondary"></i>
                          </c:otherwise>
                        </c:choose>
                      </div>
                      <div class="notification-item-content">
                        <div class="notification-item-title">${notif.title}</div>
                        <div class="notification-item-message">${notif.message}</div>
                        <div class="notification-item-time">
                          <fmt:formatDate value="${notif.createdAt}" pattern="HH:mm dd/MM" />
                        </div>
                      </div>
                      <c:if test="${not notif.read}">
                        <div class="notification-item-unread-dot"></div>
                      </c:if>
                    </a>
                  </c:forEach>
                </c:when>
                <c:when test="${not empty sessionScope.user}">
                  <div class="notification-empty">
                    <i class="bi bi-bell-slash"></i>
                    <p>Không có thông báo</p>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="notification-empty">
                    <i class="bi bi-person-circle"></i>
                    <p>Vui lòng đăng nhập để xem thông báo</p>
                    <a href="${pageContext.request.contextPath}/login" class="notification-login-btn mt-2">
                      Đăng nhập ngay
                    </a>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>

            <c:if test="${not empty sessionScope.user}">
              <div class="notification-dropdown-footer">
                <a href="${pageContext.request.contextPath}/profile/notifications"
                   class="btn btn-outline-primary btn-sm w-100">
                  <i class="bi bi-bell me-1"></i> Xem tất cả thông báo
                </a>
              </div>
            </c:if>
          </div>
        </div>
        <!-- Hotline gọi điện -->
        <a href="tel:0866448892" class="call-button">
          <div class="items-header">
            <i class="bi bi-telephone-fill fs-4 text-white"></i>
            <h5>Hotline</h5>
          </div>
        </a>

        <!-- Giỏ hàng -->
        <a href="${pageContext.request.contextPath}/cart">
          <div class="items-header">
            <i class="bi bi-cart3 fs-4 text-white"></i>
            <h5>Giỏ hàng</h5>
          </div>
        </a>


        <!-- Đăng nhập / User -->
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <div class="items-header dropdown"
                 data-bs-toggle="tooltip"
                 data-bs-placement="bottom"
                 title="${sessionScope.user.firstName} ${sessionScope.user.lastName}">
              <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown"
                      data-bs-toggle="dropdown" aria-expanded="false">
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
                                    ${sessionScope.user.firstName} ${sessionScope.user.lastName}
                                </span>
              </button>
              <ul class="dropdown-menu" aria-labelledby="userDropdown">
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
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
          </c:when>
          <c:otherwise>
            <div class="items-header login-trigger"
                 data-bs-toggle="modal"
                 data-bs-target="#smemberModal"
                 style="cursor: pointer;">
              <i class="bi bi-person-circle fs-4 text-white"></i>
              <h5>Đăng nhập</h5>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </nav>
</header>
<!-- THÔNG BÁO -->
<!-- ✅ SUCCESS MESSAGE -->
<c:if test="${not empty sessionScope.successMessage}">
  <div class="alert-container">
    <div class="container">
      <div class="alert alert-success alert-dismissible fade show auto-hide" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>
        <div class="alert-content">
          <strong>Thành công!</strong> ${sessionScope.successMessage}
        </div>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </div>
  </div>
  <c:remove var="successMessage" scope="session"/>
</c:if>

<!-- ✅ ERROR MESSAGE - FIX KHÔNG DÙNG fn:contains -->
<c:if test="${not empty sessionScope.errorMessage}">
  <c:set var="errorMsg" value="${sessionScope.errorMessage}" />
  <c:set var="shouldShowError" value="true" />

  <%-- Kiểm tra bằng Java trong JSP --%>
  <c:if test="${not empty sessionScope.user}">
    <%
      String errorMsg = (String) pageContext.getAttribute("errorMsg");
      if (errorMsg != null) {
        String lowerError = errorMsg.toLowerCase();
        boolean containsLoginMsg = lowerError.contains("vui lòng đăng nhập") ||
                lowerError.contains("đăng nhập để tiếp tục") ||
                lowerError.contains("yêu cầu đăng nhập") ||
                lowerError.contains("phải đăng nhập");

        if (containsLoginMsg) {
          pageContext.setAttribute("shouldShowError", false);
        }
      }
    %>
  </c:if>

  <c:if test="${shouldShowError}">
    <div class="alert-container">
      <div class="container">
        <div class="alert alert-danger alert-dismissible fade show auto-hide" role="alert">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <div class="alert-content">
            <strong>Lỗi!</strong> ${sessionScope.errorMessage}
          </div>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      </div>
    </div>
  </c:if>
  <c:remove var="errorMessage" scope="session"/>
</c:if>

<!-- ✅ INFO MESSAGE -->
<c:if test="${not empty sessionScope.infoMessage}">
  <div class="alert-container">
    <div class="container">
      <div class="alert alert-info alert-dismissible fade show auto-hide" role="alert">
        <i class="bi bi-info-circle-fill me-2"></i>
        <div class="alert-content">
            ${sessionScope.infoMessage}
        </div>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </div>
  </div>
  <c:remove var="infoMessage" scope="session"/>
</c:if>
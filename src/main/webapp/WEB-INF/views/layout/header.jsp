<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script>
    // Dùng để JS biết có đang login hay không
    const isLoggedIn = ${not empty sessionScope.user};
</script>

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


            <!-- Nhóm các nút bên phải: Giỏ hàng + Đăng nhập/User -->
            <div class="header-right-items">
                <div class="items-header notification-bell" id="notificationBell">
                    <i class="bi bi-bell fs-4 text-white"></i>
                    <h5>Thông báo</h5>
                    <span class="notification-badge">0</span>
                </div>
                <!-- Giỏ hàng -->
                <div class="items-header">
                    <i class="bi bi-cart3 fs-4 text-white"></i>
                    <h5>Giỏ hàng</h5>
                </div>

                <!-- Kiểm tra user đã đăng nhập chưa -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- Đã đăng nhập: Hiển thị avatar + tên -->
                        <div class="items-header dropdown"
                             data-bs-toggle="tooltip"
                             data-bs-placement="bottom"
                             title="${sessionScope.user.firstName} ${sessionScope.user.lastName}">
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
                        <!-- Chưa đăng nhập -->
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
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <strong>Thành công!</strong> ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<!-- ✅ ERROR MESSAGE -->
<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert-container">
        <div class="container">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <strong>Lỗi!</strong> ${sessionScope.errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<!-- ✅ INFO MESSAGE -->
<c:if test="${not empty sessionScope.infoMessage}">
    <div class="alert-container">
        <div class="container">
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <i class="bi bi-info-circle-fill me-2"></i>
                    ${sessionScope.infoMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </div>
    <c:remove var="infoMessage" scope="session"/>
</c:if>

<!-- CATEGORY -->
<section class="categories">
    <div class="category-items">
        <i class="bi bi-phone fs-5"></i> <h7>Điện thoại</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-laptop fs-5"></i> <h7>Laptop</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-tablet-landscape fs-5"></i> <h7>Máy tính bảng</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-earbuds fs-5"></i> <h7>Tai nghe</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-mouse fs-5"></i> <h7>Phụ kiện điện tử</h7>
    </div>
</section>

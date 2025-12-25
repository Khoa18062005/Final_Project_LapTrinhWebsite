<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>VietTech | Sàn Thương Mại Điện Tử</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS riêng - chứa toàn bộ style cho products -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body>

<!-- HEADER -->
<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">VietTech</a>

            <form class="d-flex w-50 mx-3">
                <input class="form-control me-2" type="search" placeholder="Hôm nay bạn muốn tìm gì...">
                <button class="btn-search" type="submit">
                    <i class="bi bi-search text-dark"></i>
                </button>
            </form>

            <div class="items-header">
                <h5>Giỏ hàng</h5><i class="bi bi-cart3 fs-4 text-white"></i>
            </div>

            <!-- Vendor Dashboard Link -->
            <div class="items-header" onclick="window.location.href='${pageContext.request.contextPath}/vendor'" title="Vendor Dashboard">
                <h5>Vendor</h5><i class="bi bi-shop fs-4 text-white"></i>
            </div>

<%--            <div class="items-header" onclick="window.location.href='${pageContext.request.contextPath}/register'">--%>
<%--                <h5>Đăng nhập</h5><i class="bi bi-person-circle fs-4 text-white"></i>--%>
<%--            </div>--%>
<%--            Kiểm tra đã đăng nhập hay chưa--%>
            <div class="items-header position-relative">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- ĐÃ ĐĂNG NHẬP -->
                        <div class="dropdown">
                            <a class="d-flex align-items-center text-white text-decoration-none dropdown-toggle"
                               id="dropdownUser" data-bs-toggle="dropdown" aria-expanded="false" style="cursor: pointer;">
                                <h5 class="mb-0 me-2">${sessionScope.user.fullName}</h5>
                                <i class="bi bi-person-circle fs-4"></i>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="dropdownUser">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Thông tin cá nhân</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">Đơn hàng của tôi</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- CHƯA ĐĂNG NHẬP -->
                        <div onclick="showLoginModal()" style="cursor: pointer;">
                            <h5>Đăng nhập</h5>
                            <i class="bi bi-person-circle fs-4 text-white"></i>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </nav>
</header>

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

<!-- PRODUCTS -->
<section class="products">
    <c:choose>
        <c:when test="${empty products}">
            <p class="text-center w-100 py-5 fs-4 text-muted">Hiện tại chưa có sản phẩm nào.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="phone" items="${products}">
                <div class="product">
                    <!-- Badges -->
                    <div class="product-badges">
                        <c:if test="${phone.discountPercent > 0}">
                            <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                        </c:if>
                        <span class="badge installment">Trả góp 0%</span>
                    </div>

                    <!-- Hình ảnh -->
                    <div class="product-image">
                        <img src="${pageContext.request.contextPath}/uploads/phones/${phone.primaryImage}"
                             alt="${phone.name}"
                             onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.png'">
                    </div>

                    <!-- Tên sản phẩm -->
                    <h3 class="product-name">${phone.name} | Chính hãng</h3>

                    <!-- Giá -->
                    <div class="product-price">
                        <fmt:formatNumber value="${phone.price}" type="number" groupingUsed="true"/>đ
                        <c:if test="${phone.oldPrice > phone.price}">
                            <span class="old-price">
                                <fmt:formatNumber value="${phone.oldPrice}" type="number" groupingUsed="true"/>đ
                            </span>
                        </c:if>
                    </div>

                    <!-- Giảm thêm cho member -->
                    <c:if test="${phone.memberDiscount > 0}">
                        <div class="member-discount">
                            Smember giảm đến <fmt:formatNumber value="${phone.memberDiscount}" type="number" groupingUsed="true"/>đ
                        </div>
                    </c:if>

                    <!-- Thông tin trả góp -->
                    <div class="installment-info">
                        Trả góp 0% - 0đ phụ thu - 0đ trả trước - Kỳ hạn đến 6 tháng
                    </div>

                    <!-- Footer -->
                    <div class="product-footer">
                        <div class="rating">
                            <span class="stars">★ ${phone.rating}</span>
                            <span class="like">Yêu thích</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>
<!-- Modal khuyến khích đăng nhập -->
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="loginModalLabel">Welcome to Viettech!</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center py-5">
                <i class="bi bi-person-circle fs-1 text-primary mb-3"></i>
                <p class="fs-5">Để tiếp tục sử dụng tính năng này,<br>bạn vui lòng đăng nhập hoặc đăng ký tài khoản.</p>
            </div>
            <div class="modal-footer justify-content-center">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary px-5 py-2">Đăng nhập</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-primary px-5 py-2">Đăng ký</a>
            </div>
        </div>
    </div>
</div>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
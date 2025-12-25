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

            <form class="d-flex w-50 mx-auto" action="search" method="get">
                <input class="form-control me-2" type="search" name="q" placeholder="Hôm nay bạn muốn tìm gì...">
                <button class="btn btn-warning" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>

            <div class="d-flex gap-4">
                <div class="items-header text-white text-center">
                    <i class="bi bi-cart3 fs-3"></i>
                    <h6 class="mb-0">Giỏ hàng</h6>
                </div>
                <div class="items-header text-white text-center" onclick="window.location.href='${pageContext.request.contextPath}/register'">
                    <i class="bi bi-person-circle fs-3"></i>
                    <h6 class="mb-0">Đăng nhập</h6>
                </div>
            </div>
        </div>
    </nav>
</header>

<!-- CATEGORY -->
<section class="categories bg-light py-3 border-bottom">
    <div class="container">
        <div class="d-flex justify-content-around flex-wrap gap-3">
            <div class="category-items text-center">
                <i class="bi bi-phone fs-3 text-primary"></i> <br><small>Điện thoại</small>
            </div>
            <div class="category-items text-center">
                <i class="bi bi-laptop fs-3 text-primary"></i> <br><small>Laptop</small>
            </div>
            <div class="category-items text-center">
                <i class="bi bi-tablet-landscape fs-3 text-primary"></i> <br><small>Máy tính bảng</small>
            </div>
            <div class="category-items text-center">
                <i class="bi bi-earbuds fs-3 text-primary"></i> <br><small>Tai nghe</small>
            </div>
            <div class="category-items text-center">
                <i class="bi bi-mouse fs-3 text-primary"></i> <br><small>Phụ kiện</small>
            </div>
        </div>
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

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
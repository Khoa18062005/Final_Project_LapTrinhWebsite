<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>VietTech | Sàn Thương Mại Điện Tử</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body>

<!-- Biến JavaScript để kiểm tra trạng thái đăng nhập (truyền từ server) -->
<script>
    // Dùng để JS biết có đang login hay không
    const isLoggedIn = ${not empty sessionScope.user};
</script>

<!-- HEADER -->
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
                <!-- Giỏ hàng -->
                <div class="items-header">
                    <i class="bi bi-cart3 fs-4 text-white"></i>
                    <h5>Giỏ hàng</h5>
                </div>

                <!-- Kiểm tra user đã đăng nhập chưa -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- Đã đăng nhập: Hiển thị tên user với tooltip full name -->
                        <div class="items-header dropdown"
                             data-bs-toggle="tooltip"
                             data-bs-placement="bottom"
                             title="${sessionScope.user.firstName} ${sessionScope.user.lastName}">
                            <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown"
                                    data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-person-circle me-1"></i>
                                <span class="user-name">
                                    ${sessionScope.user.firstName} ${sessionScope.user.lastName}
                                </span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                    <i class="bi bi-person"></i> Thông tin cá nhân
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
<%
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    String infoMessage = (String) session.getAttribute("infoMessage");
%>

<% if (successMessage != null && !successMessage.isEmpty()) { %>
<div class="container mt-3">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>
        <strong>Thành công!</strong> <%= successMessage %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</div>
<% session.removeAttribute("successMessage"); %>
<% } %>

<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
<div class="container mt-3">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        <strong>Lỗi!</strong> <%= errorMessage %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</div>
<% session.removeAttribute("errorMessage"); %>
<% } %>

<% if (infoMessage != null && !infoMessage.isEmpty()) { %>
<div class="container mt-3">
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        <i class="bi bi-info-circle-fill me-2"></i>
        <%= infoMessage %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</div>
<% session.removeAttribute("infoMessage"); %>
<% } %>

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
                <a href="${pageContext.request.contextPath}/product?id=${phone.id}" class="product-link">
                    <div class="product">
                        <!-- Badges -->
                        <div class="product-badges">
                            <c:if test="${phone.discountPercent > 0}">
                                <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                            </c:if>
                            <span class="badge installment">Trả góp 0%</span>
                        </div>

                        <!-- Hình ảnh: Chỉ hiển thị nếu primaryImage không rỗng -->
                        <c:if test="${not empty phone.primaryImage}">
                            <div class="product-image">
                                <img src="${pageContext.request.contextPath}/uploads/phones/${phone.primaryImage}"
                                     alt="${phone.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.png'">
                            </div>
                        </c:if>
                        <!-- Nếu không có ảnh, có thể thêm placeholder text nếu muốn -->
                        <c:if test="${empty phone.primaryImage}">
                            <div class="product-image-placeholder text-center py-3">
                                <p class="text-muted">Chưa có ảnh sản phẩm</p>
                            </div>
                        </c:if>

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
                </a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>

<!-- Modal Smember - Phiên bản ĐẸP LUNG LINH 2025 -->
<!-- Modal Smember - Minimalist & Đẹp như CellphoneS -->
<div class="modal fade" id="smemberModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content smember-modal-content">
            <!-- Nút đóng góc trên PHẢI -->
            <button type="button" class="btn-close smember-close-btn" data-bs-dismiss="modal" aria-label="Close"></button>

            <div class="modal-body text-center py-5 px-4">
                <!-- Tiêu đề Smember gradient xanh -->
                <h1 class="smember-title mb-5">Smember</h1>

                <!-- Nội dung -->
                <p class="smember-text mb-5">
                    Vui lòng đăng nhập tài khoản Smember để<br>
                    <strong>xem ưu đãi và thanh toán dễ dàng hơn.</strong>
                </p>

                <!-- Hai nút pill -->
                <div class="d-flex flex-column flex-sm-row justify-content-center gap-4">
                    <a href="${pageContext.request.contextPath}/register" class="smember-btn-register">
                        Đăng ký
                    </a>
                    <a href="${pageContext.request.contextPath}/login" class="smember-btn-login">
                        Đăng nhập
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<!-- Script riêng cho popup login -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>

</body>
</html>
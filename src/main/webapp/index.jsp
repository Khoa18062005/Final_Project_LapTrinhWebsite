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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">

</head>
<body>
<%@ include file="header.jsp" %>
<!-- Biến JavaScript để kiểm tra trạng thái đăng nhập (truyền từ server) -->
<script>
    // Biến toàn cục cho JavaScript
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>

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
    <a class="category-items" href="#dien-thoai">
        <i class="bi bi-phone fs-5"></i> <h7>Điện thoại</h7>
    </a>
    <a class="category-items" href="#laptop">
        <i class="bi bi-laptop fs-5"></i> <h7>Laptop</h7>
    </a>
    <a class="category-items" href="#may-tinh-bang">
        <i class="bi bi-tablet-landscape fs-5"></i> <h7>Máy tính bảng</h7>
    </a>
    <a class="category-items" href="#tai-nghe">
        <i class="bi bi-earbuds fs-5"></i> <h7>Tai nghe</h7>
    </a>
    <a class="category-items" href="#phu-kien">
        <i class="bi bi-mouse fs-5"></i> <h7>Phụ kiện điện tử</h7>
    </a>
</section>

<!-- PRODUCTS -->
<section class="products">

    <!-- ==================== ĐIỆN THOẠI ==================== -->
    <div class="category-block mb-5">
        <h2 id="dien-thoai" class="category-title text-center mb-4">Điện thoại</h2>
        <c:choose>
            <c:when test="${empty phones}">
                <p class="text-center w-100 py-5 fs-4 text-muted">Hiện tại chưa có điện thoại nào.</p>
            </c:when>
            <c:otherwise>
                <div class="row justify-content-start">
                    <c:forEach var="phone" items="${phones}">
                        <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
                            <a href="${pageContext.request.contextPath}/product?id=${phone.id}" class="product-link">
                                <div class="product position-relative">

                                    <!-- Badges -->
                                    <div class="product-badges">
                                        <c:if test="${phone.discountPercent > 0}">
                                            <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                                        </c:if>
                                        <span class="badge installment">Trả góp 0%</span>
                                    </div>

                                    <!-- Hình ảnh từ CDN -->
                                    <c:choose>
                                        <c:when test="${not empty phone.primaryImage}">
                                            <div class="product-image">
                                                <img src="${phone.primaryImage}"
                                                     alt="${phone.name}"
                                                     class="w-100 h-100"
                                                     style="object-fit: contain;"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';">
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center">
                                                <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

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
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- ==================== LAPTOP ==================== -->
    <div class="category-block mb-5">
        <h2 id="laptop" class="category-title text-center mb-4">Laptop</h2>
        <c:choose>
            <c:when test="${empty laptops}">
                <p class="text-center w-100 py-5 fs-4 text-muted">Hiện tại chưa có laptop nào.</p>
            </c:when>
            <c:otherwise>
                <div class="row justify-content-start">
                    <c:forEach var="phone" items="${laptops}">
                        <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
                            <a href="${pageContext.request.contextPath}/product?id=${phone.id}" class="product-link">
                                <div class="product position-relative">
                                    <!-- Badges -->
                                    <div class="product-badges">
                                        <c:if test="${phone.discountPercent > 0}">
                                            <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                                        </c:if>
                                        <span class="badge installment">Trả góp 0%</span>
                                    </div>

                                    <!-- Hình ảnh từ CDN -->
                                    <c:choose>
                                        <c:when test="${not empty phone.primaryImage}">
                                            <div class="product-image">
                                                <img src="${phone.primaryImage}"
                                                     alt="${phone.name}"
                                                     class="w-100 h-100"
                                                     style="object-fit: contain;"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';">
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center">
                                                <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <h3 class="product-name">${phone.name} | Chính hãng</h3>
                                    <div class="product-price">
                                        <fmt:formatNumber value="${phone.price}" type="number" groupingUsed="true"/>đ
                                        <c:if test="${phone.oldPrice > phone.price}">
                                            <span class="old-price">
                                                <fmt:formatNumber value="${phone.oldPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </c:if>
                                    </div>
                                    <c:if test="${phone.memberDiscount > 0}">
                                        <div class="member-discount">
                                            Smember giảm đến <fmt:formatNumber value="${phone.memberDiscount}" type="number" groupingUsed="true"/>đ
                                        </div>
                                    </c:if>
                                    <div class="installment-info">
                                        Trả góp 0% - 0đ phụ thu - 0đ trả trước - Kỳ hạn đến 6 tháng
                                    </div>
                                    <div class="product-footer">
                                        <div class="rating">
                                            <span class="stars">★ ${phone.rating}</span>
                                            <span class="like">Yêu thích</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- ==================== MÁY TÍNH BẢNG ==================== -->
    <div class="category-block mb-5">
        <h2 id="may-tinh-bang" class="category-title text-center mb-4">Máy tính bảng</h2>
        <c:choose>
            <c:when test="${empty tablets}">
                <p class="text-center w-100 py-5 fs-4 text-muted">Hiện tại chưa có máy tính bảng nào.</p>
            </c:when>
            <c:otherwise>
                <div class="row justify-content-start">
                    <c:forEach var="phone" items="${tablets}">
                        <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
                            <a href="${pageContext.request.contextPath}/product?id=${phone.id}" class="product-link">
                                <div class="product position-relative">
                                    <!-- Badges + Ảnh + Nội dung giống hệt trên -->
                                    <div class="product-badges">
                                        <c:if test="${phone.discountPercent > 0}">
                                            <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                                        </c:if>
                                        <span class="badge installment">Trả góp 0%</span>
                                    </div>

                                    <c:choose>
                                        <c:when test="${not empty phone.primaryImage}">
                                            <div class="product-image">
                                                <img src="${phone.primaryImage}"
                                                     alt="${phone.name}"
                                                     class="w-100 h-100"
                                                     style="object-fit: contain;"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';">
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center">
                                                <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <h3 class="product-name">${phone.name} | Chính hãng</h3>
                                    <div class="product-price">
                                        <fmt:formatNumber value="${phone.price}" type="number" groupingUsed="true"/>đ
                                        <c:if test="${phone.oldPrice > phone.price}">
                                            <span class="old-price">
                                                <fmt:formatNumber value="${phone.oldPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </c:if>
                                    </div>
                                    <c:if test="${phone.memberDiscount > 0}">
                                        <div class="member-discount">
                                            Smember giảm đến <fmt:formatNumber value="${phone.memberDiscount}" type="number" groupingUsed="true"/>đ
                                        </div>
                                    </c:if>
                                    <div class="installment-info">
                                        Trả góp 0% - 0đ phụ thu - 0đ trả trước - Kỳ hạn đến 6 tháng
                                    </div>
                                    <div class="product-footer">
                                        <div class="rating">
                                            <span class="stars">★ ${phone.rating}</span>
                                            <span class="like">Yêu thích</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- ==================== TAI NGHE ==================== -->
    <div class="category-block mb-5">
        <h2 id="tai-nghe" class="category-title text-center mb-4">Tai nghe</h2>
        <c:choose>
            <c:when test="${empty headphones}">
                <p class="text-center w-100 py-5 fs-4 text-muted">Hiện tại chưa có tai nghe nào.</p>
            </c:when>
            <c:otherwise>
                <div class="row justify-content-start">
                    <c:forEach var="phone" items="${headphones}">
                        <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
                            <a href="${pageContext.request.contextPath}/product?id=${phone.id}" class="product-link">
                                <div class="product position-relative">
                                    <div class="product-badges">
                                        <c:if test="${phone.discountPercent > 0}">
                                            <span class="badge discount">Giảm ${phone.discountPercent}%</span>
                                        </c:if>
                                        <span class="badge installment">Trả góp 0%</span>
                                    </div>

                                    <c:choose>
                                        <c:when test="${not empty phone.primaryImage}">
                                            <div class="product-image">
                                                <img src="${phone.primaryImage}"
                                                     alt="${phone.name}"
                                                     class="w-100 h-100"
                                                     style="object-fit: contain;"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';">
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center">
                                                <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <h3 class="product-name">${phone.name} | Chính hãng</h3>
                                    <div class="product-price">
                                        <fmt:formatNumber value="${phone.price}" type="number" groupingUsed="true"/>đ
                                        <c:if test="${phone.oldPrice > phone.price}">
                                            <span class="old-price">
                                                <fmt:formatNumber value="${phone.oldPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </c:if>
                                    </div>
                                    <c:if test="${phone.memberDiscount > 0}">
                                        <div class="member-discount">
                                            Smember giảm đến <fmt:formatNumber value="${phone.memberDiscount}" type="number" groupingUsed="true"/>đ
                                        </div>
                                    </c:if>
                                    <div class="installment-info">
                                        Trả góp 0% - 0đ phụ thu - 0đ trả trước - Kỳ hạn đến 6 tháng
                                    </div>
                                    <div class="product-footer">
                                        <div class="rating">
                                            <span class="stars">★ ${phone.rating}</span>
                                            <span class="like">Yêu thích</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>
<!-- Modal Smember -->
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
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>


</body>
</html>
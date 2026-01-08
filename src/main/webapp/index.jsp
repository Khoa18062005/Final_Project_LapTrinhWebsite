<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi" prefix="og: http://ogp.me/ns#">
<head>
    <!-- ===== META TAGS CƠ BẢN ===== -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <!-- ===== TITLE & DESCRIPTION ===== -->
    <title>VietTech Store - Siêu Thị Điện Tử Trực Tuyến | Điện Thoại, Laptop, Máy Tính Bảng Chính Hãng</title>
    <meta name="description" content="VietTech - Siêu thị điện tử trực tuyến uy tín hàng đầu Việt Nam. Chuyên cung cấp điện thoại, laptop, máy tính bảng, tai nghe chính hãng với giá tốt nhất. Trả góp 0%, freeship toàn quốc, bảo hành chính hãng.">
    <meta name="keywords" content="VietTech, siêu thị điện tử, điện thoại chính hãng, laptop giá rẻ, máy tính bảng, tai nghe bluetooth, phụ kiện điện tử, mua điện thoại online, laptop gaming, iPhone chính hãng, Samsung, trả góp 0%">
    <meta name="author" content="VietTech Store">
    <meta name="robots" content="index, follow, max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <meta name="googlebot" content="index, follow">
    <meta name="language" content="Vietnamese">
    <meta name="geo.region" content="VN-SG">
    <meta name="geo.placename" content="Ho Chi Minh City">
    <meta name="geo.position" content="10.850632;106.772087">
    <meta name="ICBM" content="10.850632, 106.772087">

    <!-- ===== OPEN GRAPH (FACEBOOK) ===== -->
    <meta property="og:type" content="website">
    <meta property="og:url" content="https://viettechstore.online">
    <meta property="og:title" content="VietTech Store - Siêu Thị Điện Tử Trực Tuyến Hàng Đầu Việt Nam">
    <meta property="og:description" content="Mua điện thoại, laptop, máy tính bảng chính hãng giá tốt tại VietTech. Trả góp 0%, freeship, bảo hành chính hãng.">
    <meta property="og:image" content="https://viettechstore.online/assets/PNG/LogoVT.png">
    <meta property="og:image:alt" content="VietTech Store Logo">
    <meta property="og:image:width" content="1200">
    <meta property="og:image:height" content="630">
    <meta property="og:site_name" content="VietTech Store">
    <meta property="og:locale" content="vi_VN">

    <!-- ===== TWITTER CARD ===== -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@viettechstore">
    <meta name="twitter:title" content="VietTech Store - Siêu Thị Điện Tử Trực Tuyến">
    <meta name="twitter:description" content="Mua điện thoại, laptop, máy tính bảng chính hãng giá tốt. Trả góp 0%, freeship toàn quốc.">
    <meta name="twitter:image" content="https://viettechstore.online/assets/PNG/LogoVT.png">

    <!-- ===== CANONICAL & ALTERNATE ===== -->
    <link rel="canonical" href="https://viettechstore.online/">
    <link rel="alternate" hreflang="vi-VN" href="https://viettechstore.online/">
    <link rel="alternate" hreflang="x-default" href="https://viettechstore.online/">

    <!-- ===== FAVICON ===== -->
    <link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">

    <!-- ===== PRECONNECT & DNS-PREFETCH ===== -->
    <link rel="preconnect" href="https://cdn.jsdelivr.net" crossorigin>
    <link rel="dns-prefetch" href="https://cdn.jsdelivr.net">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="dns-prefetch" href="https://fonts.googleapis.com">

    <!-- ===== PRELOAD CRITICAL RESOURCES ===== -->
    <link rel="preload" href="${pageContext.request.contextPath}/assets/css/main.css" as="style">
    <link rel="preload" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" as="style">

    <!-- ===== BOOTSTRAP ===== -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- ===== CSS RIÊNG ===== -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">

    <!-- ===== STRUCTURED DATA (JSON-LD) ===== -->
    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "Store",
            "name": "VietTech Store",
            "description": "Siêu thị điện tử trực tuyến uy tín hàng đầu Việt Nam",
            "url": "https://viettechstore.online",
            "logo": "https://viettechstore.online/assets/PNG/LogoVT.png",
            "image": "https://viettechstore.online/assets/PNG/LogoVT.png",
            "telephone": "+84866448892",
            "email": "support@viettech.vn",
            "address": {
                "@type": "PostalAddress",
                "streetAddress": "01 Đường Võ Văn Ngân, Linh Chiểu",
                "addressLocality": "Thủ Đức",
                "addressRegion": "TP. Hồ Chí Minh",
                "addressCountry": "VN"
            },
            "geo": {
                "@type": "GeoCoordinates",
                "latitude": "10.850632",
                "longitude": "106.772087"
            },
            "openingHoursSpecification": [
                {
                    "@type": "OpeningHoursSpecification",
                    "dayOfWeek": [
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday",
                        "Sunday"
                    ],
                    "opens": "08:00",
                    "closes": "22:00"
                }
            ],
            "priceRange": "$$",
            "paymentAccepted": "Cash, Credit Card, MoMo, ZaloPay, VNPay",
            "sameAs": [
                "https://www.facebook.com/viettechstore",
                "https://www.instagram.com/viettechstore"
            ]
        }
    </script>

    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "WebSite",
            "name": "VietTech Store",
            "url": "https://viettechstore.online",
            "potentialAction": {
                "@type": "SearchAction",
                "target": "https://viettechstore.online/search?q={search_term_string}",
                "query-input": "required name=search_term_string"
            }
        }
    </script>

    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "Organization",
            "name": "VietTech Store",
            "url": "https://viettechstore.online",
            "logo": "https://viettechstore.online/assets/PNG/LogoVT.png",
            "contactPoint": {
                "@type": "ContactPoint",
                "telephone": "+84866448892",
                "contactType": "Customer Service",
                "areaServed": "VN",
                "availableLanguage": "Vietnamese"
            }
        }
    </script>

</head>
<body>
<%@ include file="header.jsp" %>
<!-- Biến JavaScript để kiểm tra trạng thái đăng nhập (truyền từ server) -->
<script>
    // Biến toàn cục cho JavaScript
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>

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

<jsp:include page="footer.jsp" />
<!-- Script riêng cho popup login -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<!-- VietTech ChatBot -->
<script src="${pageContext.request.contextPath}/assets/js/chatbot.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/search-suggestions.js"></script>

</body>
</html>
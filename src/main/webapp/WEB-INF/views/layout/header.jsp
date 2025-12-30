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

<script>
    const isLoggedIn = ${not empty sessionScope.user};
</script>

<!-- HEADER -->
<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">

            <!-- Logo -->
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/PNG/LogoVT.png"
                     alt="VietTech Logo" height="60">
            </a>

            <!-- Search -->
            <form class="d-flex w-50 mx-3" action="${pageContext.request.contextPath}/search">
                <input class="form-control me-2" type="search" name="keyword"
                       placeholder="Hôm nay bạn muốn tìm gì...">
                <button class="btn-search" type="submit">
                    <i class="bi bi-search text-dark"></i>
                </button>
            </form>

            <!-- Right items -->
            <div class="header-right-items">

                <!-- Cart -->
                <a href="${pageContext.request.contextPath}/cart" class="items-header text-decoration-none text-white">
                    <i class="bi bi-cart3 fs-4"></i>
                    <h5>Giỏ hàng</h5>
                </a>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="items-header dropdown"
                             data-bs-toggle="tooltip"
                             title="${sessionScope.user.firstName} ${sessionScope.user.lastName}">
                            <button class="btn btn-light dropdown-toggle"
                                    data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i>
                                    ${sessionScope.user.firstName}
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person"></i> Thông tin cá nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                        <i class="bi bi-box"></i> Đơn hàng của tôi
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger"
                                       href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right"></i> Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="items-header login-trigger"
                             data-bs-toggle="modal"
                             data-bs-target="#smemberModal">
                            <i class="bi bi-person-circle fs-4"></i>
                            <h5>Đăng nhập</h5>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </nav>
</header>

<!-- CATEGORY -->
<section class="categories">
    <a href="${pageContext.request.contextPath}/category/phone" class="category-items">
        <i class="bi bi-phone"></i><span>Điện thoại</span>
    </a>
    <a href="${pageContext.request.contextPath}/category/laptop" class="category-items">
        <i class="bi bi-laptop"></i><span>Laptop</span>
    </a>
    <a href="${pageContext.request.contextPath}/category/tablet" class="category-items">
        <i class="bi bi-tablet-landscape"></i><span>Máy tính bảng</span>
    </a>
    <a href="${pageContext.request.contextPath}/category/headphone" class="category-items">
        <i class="bi bi-earbuds"></i><span>Tai nghe</span>
    </a>
    <a href="${pageContext.request.contextPath}/category/accessory" class="category-items">
        <i class="bi bi-mouse"></i><span>Phụ kiện</span>
    </a>
</section>

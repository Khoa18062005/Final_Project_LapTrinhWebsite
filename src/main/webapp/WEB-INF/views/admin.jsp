<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - VietTech</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="sidebar">
    <div class="logo">
        <h2>Viet<span>Tech</span></h2>
    </div>
    <nav class="nav-menu">
        <div class="nav-menu-section">
            <a href="#dashboard" class="nav-item active" onclick="showSection('dashboard')">
                <i class="fas fa-chart-pie"></i>
                <span>Dashboard</span>
            </a>
            <a href="#products" class="nav-item" onclick="showSection('products')">
                <i class="fas fa-shopping-bag"></i>
                <span>Sản phẩm</span>
            </a>
            <a href="#orders" class="nav-item" onclick="showSection('orders')">
                <i class="fas fa-list-ul"></i>
                <span>Đơn hàng</span>
            </a>
            <a href="#users" class="nav-item" onclick="showSection('users')">
                <i class="fas fa-users"></i>
                <span>Người dùng</span>
            </a>
        </div>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout" class="nav-item">
            <i class="fas fa-sign-out-alt"></i>
            <span>Đăng xuất</span>
        </a>
    </div>
</div>

<div class="main-content">
    <div class="top-navbar">
        <div class="navbar-left">
            <button class="menu-toggle" onclick="toggleSidebar()">
                <i class="fas fa-bars"></i>
            </button>
            <h1 id="page-title">Dashboard</h1>
        </div>
        <div class="navbar-right">
            <div class="user-profile">
                <img src="${pageContext.request.contextPath}/assets/PNG/AVT.png" alt="Admin">
                <div class="user-profile-info">
                    <div class="name">${sessionScope.user.firstName} ${sessionScope.user.lastName}</div>
                    <div class="role">Admin</div>
                </div>
            </div>
        </div>
    </div>

    <div id="dashboard" class="content-section active">
        <jsp:include page="/WEB-INF/views/admin_pages/dashboard.jsp" />
    </div>

    <div id="products" class="content-section">
        <jsp:include page="/WEB-INF/views/admin_pages/products.jsp" />
    </div>

    <div id="orders" class="content-section">
<%--        <jsp:include page="/WEB-INF/views/admin_pages/orders.jsp" />--%>
    </div>

    <div id="users" class="content-section">
<%--        <jsp:include page="/WEB-INF/views/admin_pages/u sers.jsp" />--%>
    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>